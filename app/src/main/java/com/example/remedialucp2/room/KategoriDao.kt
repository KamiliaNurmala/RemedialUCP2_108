package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {
    @Query("SELECT * FROM tblKategori WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getAllKategori(): Flow<List<Kategori>>

    @Query("SELECT * FROM tblKategori WHERE id = :id")
    fun getKategoriById(id: Int): Flow<Kategori?>

    @Query("SELECT * FROM tblKategori WHERE parentId = :parentId AND isDeleted = 0")
    fun getSubKategori(parentId: Int): Flow<List<Kategori>>

    @Query("SELECT * FROM tblKategori WHERE parentId IS NULL AND isDeleted = 0")
    fun getRootKategori(): Flow<List<Kategori>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(kategori: Kategori): Long

    @Update
    suspend fun update(kategori: Kategori)

    @Delete
    suspend fun delete(kategori: Kategori)

    @Query("SELECT * FROM tblKategori WHERE parentId = :parentId AND isDeleted = 0")
    suspend fun getSubKategoriList(parentId: Int): List<Kategori>
}
