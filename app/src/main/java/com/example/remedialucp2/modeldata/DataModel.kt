package com.example.remedialucp2.modeldata

import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.room.Kategori

// ==================== BUKU ====================
data class DetailBuku(
    val id: Int = 0,
    val judul: String = "",
    val isbn: String = "",
    val tahunTerbit: String = "",
    val kategoriId: Int? = null,
    val statusPinjam: Boolean = false,
    val pengarangIds: List<Int> = emptyList()
)

data class UIStateBuku(
    val detailBuku: DetailBuku = DetailBuku(),
    val isEntryValid: Boolean = false
)

fun DetailBuku.toBuku(): Buku = Buku(
    id = id,
    judul = judul,
    isbn = isbn,
    tahunTerbit = tahunTerbit.toIntOrNull() ?: 0,
    kategoriId = kategoriId,
    statusPinjam = statusPinjam
)

fun Buku.toDetailBuku(): DetailBuku = DetailBuku(
    id = id,
    judul = judul,
    isbn = isbn,
    tahunTerbit = tahunTerbit.toString(),
    kategoriId = kategoriId,
    statusPinjam = statusPinjam
)

fun Buku.toUiStateBuku(isEntryValid: Boolean = false): UIStateBuku = UIStateBuku(
    detailBuku = this.toDetailBuku(),
    isEntryValid = isEntryValid
)

// ==================== PENGARANG ====================
data class DetailPengarang(
    val id: Int = 0,
    val nama: String = "",
    val negara: String = ""
)

data class UIStatePengarang(
    val detailPengarang: DetailPengarang = DetailPengarang(),
    val isEntryValid: Boolean = false
)

fun DetailPengarang.toPengarang(): Pengarang = Pengarang(
    id = id,
    nama = nama,
    negara = negara
)

fun Pengarang.toDetailPengarang(): DetailPengarang = DetailPengarang(
    id = id,
    nama = nama,
    negara = negara
)

fun Pengarang.toUiStatePengarang(isEntryValid: Boolean = false): UIStatePengarang = UIStatePengarang(
    detailPengarang = this.toDetailPengarang(),
    isEntryValid = isEntryValid
)

// ==================== KATEGORI ====================
data class DetailKategori(
    val id: Int = 0,
    val nama: String = "",
    val parentId: Int? = null
)

data class UIStateKategori(
    val detailKategori: DetailKategori = DetailKategori(),
    val isEntryValid: Boolean = false
)

fun DetailKategori.toKategori(): Kategori = Kategori(
    id = id,
    nama = nama,
    parentId = parentId
)

fun Kategori.toDetailKategori(): DetailKategori = DetailKategori(
    id = id,
    nama = nama,
    parentId = parentId
)

fun Kategori.toUiStateKategori(isEntryValid: Boolean = false): UIStateKategori = UIStateKategori(
    detailKategori = this.toDetailKategori(),
    isEntryValid = isEntryValid
)
