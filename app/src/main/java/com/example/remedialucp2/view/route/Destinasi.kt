package com.example.remedialucp2.view.route

import com.example.remedialucp2.R

object DestinasiHome : DestinasiNavigasi {
    override val route = "home"
    override val titleRes = R.string.app_name
}

object DestinasiEntryBuku : DestinasiNavigasi {
    override val route = "entry_buku"
    override val titleRes = R.string.tambah_buku
}

object DestinasiEntryPengarang : DestinasiNavigasi {
    override val route = "entry_pengarang"
    override val titleRes = R.string.tambah_pengarang
}

object DestinasiEntryKategori : DestinasiNavigasi {
    override val route = "entry_kategori"
    override val titleRes = R.string.tambah_kategori
}

object DestinasiDetailBuku : DestinasiNavigasi {
    override val route = "detail_buku"
    override val titleRes = R.string.detail_buku
    const val bukuIdArg = "bukuId"
    val routeWithArgs = "$route/{$bukuIdArg}"
}

object DestinasiDetailPengarang : DestinasiNavigasi {
    override val route = "detail_pengarang"
    override val titleRes = R.string.detail_pengarang
    const val pengarangIdArg = "pengarangId"
    val routeWithArgs = "$route/{$pengarangIdArg}"
}

object DestinasiDetailKategori : DestinasiNavigasi {
    override val route = "detail_kategori"
    override val titleRes = R.string.detail_kategori
    const val kategoriIdArg = "kategoriId"
    val routeWithArgs = "$route/{$kategoriIdArg}"
}
