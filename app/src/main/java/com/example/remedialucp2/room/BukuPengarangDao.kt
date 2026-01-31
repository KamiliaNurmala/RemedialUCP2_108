package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BukuPengarangDao {
    @Query("SELECT * FROM tblBukuPengarang WHERE bukuId = :bukuId")
    fun getPengarangByBuku(bukuId: Int): Flow<List<BukuPengarang>>

    @Query("SELECT * FROM tblBukuPengarang WHERE pengarangId = :pengarangId")
    fun getBukuByPengarang(pengarangId: Int): Flow<List<BukuPengarang>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bukuPengarang: BukuPengarang)

    @Delete
    suspend fun delete(bukuPengarang: BukuPengarang)

    @Query("DELETE FROM tblBukuPengarang WHERE bukuId = :bukuId")
    suspend fun deleteByBuku(bukuId: Int)
}
