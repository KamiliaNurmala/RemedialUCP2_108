package com.example.remedialucp2.repositori

import androidx.room.withTransaction
import com.example.remedialucp2.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RepositoriPerpustakaan(
    private val bukuDao: BukuDao,
    private val pengarangDao: PengarangDao,
    private val kategoriDao: KategoriDao,
    private val bukuPengarangDao: BukuPengarangDao,
    private val database: DatabasePerpustakaan
) {
    // ==================== BUKU ====================
    fun getAllBuku(): Flow<List<Buku>> = bukuDao.getAllBuku()
    fun getBukuById(id: Int): Flow<Buku?> = bukuDao.getBukuById(id)
    fun getBukuByKategori(kategoriId: Int): Flow<List<Buku>> = bukuDao.getBukuByKategori(kategoriId)

    suspend fun insertBuku(buku: Buku, pengarangIds: List<Int>): Long {
        val id = bukuDao.insert(buku)
        // Insert relasi buku-pengarang
        pengarangIds.forEach { pengarangId ->
            bukuPengarangDao.insert(BukuPengarang(id.toInt(), pengarangId))
        }
        return id
    }

    suspend fun updateBuku(buku: Buku, pengarangIds: List<Int>) {
        val oldBuku = bukuDao.getBukuById(buku.id).first()
        bukuDao.update(buku.copy(updatedAt = System.currentTimeMillis()))
        // Update relasi
        bukuPengarangDao.deleteByBuku(buku.id)
        pengarangIds.forEach { pengarangId ->
            bukuPengarangDao.insert(BukuPengarang(buku.id, pengarangId))
        }
        // Audit log
        auditLogDao.insert(AuditLog(
            tableName = "tblBuku",
            recordId = buku.id,
            action = "UPDATE",
            dataBefore = oldBuku.toString(),
            dataAfter = buku.toString()
        ))
    }

    suspend fun softDeleteBuku(id: Int) {
        val oldBuku = bukuDao.getBukuById(id).first()
        bukuDao.softDelete(id)
        auditLogDao.insert(AuditLog(
            tableName = "tblBuku",
            recordId = id,
            action = "DELETE",
            dataBefore = oldBuku.toString(),
            dataAfter = null
        ))
    }

    // ==================== PENGARANG ====================
    fun getAllPengarang(): Flow<List<Pengarang>> = pengarangDao.getAllPengarang()
    fun getPengarangById(id: Int): Flow<Pengarang?> = pengarangDao.getPengarangById(id)
    fun getPengarangByBuku(bukuId: Int): Flow<List<BukuPengarang>> = bukuPengarangDao.getPengarangByBuku(bukuId)

    suspend fun insertPengarang(pengarang: Pengarang): Long {
        val id = pengarangDao.insert(pengarang)

        return id
    }

    suspend fun updatePengarang(pengarang: Pengarang) {
        val oldPengarang = pengarangDao.getPengarangById(pengarang.id).first()
        pengarangDao.update(pengarang)

    }

    suspend fun softDeletePengarang(id: Int) {
        val oldPengarang = pengarangDao.getPengarangById(id).first()
        pengarangDao.softDelete(id)

    }

    // ==================== KATEGORI ====================
    fun getAllKategori(): Flow<List<Kategori>> = kategoriDao.getAllKategori()
    fun getKategoriById(id: Int): Flow<Kategori?> = kategoriDao.getKategoriById(id)
    fun getRootKategori(): Flow<List<Kategori>> = kategoriDao.getRootKategori()
    fun getSubKategori(parentId: Int): Flow<List<Kategori>> = kategoriDao.getSubKategori(parentId)

    suspend fun insertKategori(kategori: Kategori): Long {
        // Cek cyclic reference
        if (kategori.parentId != null && wouldCreateCycle(kategori.id, kategori.parentId)) {
            throw IllegalArgumentException("Cyclic reference detected!")
        }
        val id = kategoriDao.insert(kategori)
        auditLogDao.insert(AuditLog(
            tableName = "tblKategori",
            recordId = id.toInt(),
            action = "INSERT",
            dataBefore = null,
            dataAfter = kategori.toString()
        ))
        return id
    }

    suspend fun updateKategori(kategori: Kategori) {
        // Cek cyclic reference
        if (kategori.parentId != null && wouldCreateCycle(kategori.id, kategori.parentId)) {
            throw IllegalArgumentException("Cyclic reference detected!")
        }
        val oldKategori = kategoriDao.getKategoriById(kategori.id).first()
        kategoriDao.update(kategori)
    }

    // Check if setting parentId would create a cycle
    private suspend fun wouldCreateCycle(kategoriId: Int, parentId: Int): Boolean {
        if (kategoriId == parentId) return true
        var currentParentId: Int? = parentId
        while (currentParentId != null) {
            if (currentParentId == kategoriId) return true
            val parent = kategoriDao.getKategoriById(currentParentId).first()
            currentParentId = parent?.parentId
        }
        return false
    }

    /**
     * Delete kategori with transaction
     * - If books are borrowed, rollback
     * - If books not borrowed, option to delete or set to "Tanpa Kategori"
     */
    suspend fun deleteKategoriWithTransaction(
        kategoriId: Int,
        deleteBooksAlso: Boolean
    ): Result<Unit> {
        return try {
            database.withTransaction {
                // Get all sub-categories recursively
                val allKategoriIds = getAllSubKategoriIds(kategoriId) + kategoriId

                // Check if any book in these categories is borrowed
                for (kId in allKategoriIds) {
                    val borrowedBooks = bukuDao.getBukuDipinjamByKategori(kId)
                    if (borrowedBooks.isNotEmpty()) {
                        throw IllegalStateException("Cannot delete: Books are still borrowed!")
                    }
                }

                // Proceed with deletion
                for (kId in allKategoriIds) {
                    if (deleteBooksAlso) {
                        bukuDao.softDeleteBukuByKategori(kId)
                    } else {
                        bukuDao.setBukuTanpaKategori(kId)
                    }
                    val oldKategori = kategoriDao.getKategoriById(kId).first()
                    kategoriDao.softDelete(kId)

                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getAllSubKategoriIds(parentId: Int): List<Int> {
        val result = mutableListOf<Int>()
        val subKategori = kategoriDao.getSubKategoriList(parentId)
        for (kat in subKategori) {
            result.add(kat.id)
            result.addAll(getAllSubKategoriIds(kat.id))
        }
        return result
    }

}
