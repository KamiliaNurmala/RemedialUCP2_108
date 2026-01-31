package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblBuku")
data class Buku(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val judul: String,
    val isbn: String,
    val tahunTerbit: Int,
    val kategoriId: Int?, // null = "Tanpa Kategori"
    val statusPinjam: Boolean = false,
    val isDeleted: Boolean = false, // Soft Delete
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
