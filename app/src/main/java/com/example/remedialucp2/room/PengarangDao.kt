package com.example.remedialucp2.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PengarangDao {
    @Query("SELECT * FROM tblPengarang WHERE isDeleted = 0 ORDER BY nama ASC")
    fun getAllPengarang(): Flow<List<Pengarang>>

    @Query("SELECT * FROM tblPengarang WHERE id = :id")
    fun getPengarangById(id: Int): Flow<Pengarang?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pengarang: Pengarang): Long

    @Update
    suspend fun update(pengarang: Pengarang)

    @Delete
    suspend fun delete(pengarang: Pengarang)

    @Query("UPDATE tblPengarang SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)
}
