package com.example.remedialucp2.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblPengarang")
data class Pengarang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val negara: String,
    val isDeleted: Boolean = false
)
