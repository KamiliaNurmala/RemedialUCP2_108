package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblKategori")
data class Kategori(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val parentId: Int? = null, // Hierarchical - null means root category
    val isDeleted: Boolean = false
)
