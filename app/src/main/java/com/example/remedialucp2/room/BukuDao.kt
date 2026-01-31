package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuDao {
    @Query("SELECT * FROM tblBuku WHERE isDeleted = 0 ORDER BY judul ASC")
    fun getAllBuku(): Flow<List<Buku>>

    @Query("SELECT * FROM tblBuku WHERE id = :id")
    fun getBukuById(id: Int): Flow<Buku?>

    @Query("SELECT * FROM tblBuku WHERE kategoriId = :kategoriId AND isDeleted = 0")
    fun getBukuByKategori(kategoriId: Int): Flow<List<Buku>>

    @Query("SELECT * FROM tblBuku WHERE kategoriId = :kategoriId AND statusPinjam = 1 AND isDeleted = 0")
    suspend fun getBukuDipinjamByKategori(kategoriId: Int): List<Buku>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(buku: Buku): Long

    @Update
    suspend fun update(buku: Buku)

    @Delete
    suspend fun delete(buku: Buku)

    @Query("UPDATE tblBuku SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun softDelete(id: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE tblBuku SET kategoriId = NULL WHERE kategoriId = :kategoriId")
    suspend fun setBukuTanpaKategori(kategoriId: Int)

    @Query("UPDATE tblBuku SET isDeleted = 1 WHERE kategoriId = :kategoriId")
    suspend fun softDeleteBukuByKategori(kategoriId: Int)
}
