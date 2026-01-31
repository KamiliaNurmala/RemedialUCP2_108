package com.example.remedialucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.remedialucp2.modeldata.DetailBuku
import com.example.remedialucp2.modeldata.UIStateBuku
import com.example.remedialucp2.modeldata.toBuku
import com.example.remedialucp2.repositori.RepositoriPerpustakaan

class EntryBukuViewModel(private val repositori: RepositoriPerpustakaan) : ViewModel() {
    var uiStateBuku by mutableStateOf(UIStateBuku())
        private set

    private fun validasiInput(uiState: DetailBuku = uiStateBuku.detailBuku): Boolean {
        return with(uiState) {
            judul.isNotBlank() && isbn.isNotBlank() && tahunTerbit.isNotBlank() &&
            tahunTerbit.toIntOrNull() != null && tahunTerbit.toInt() > 1900
        }
    }

    fun updateUiState(detailBuku: DetailBuku) {
        uiStateBuku = UIStateBuku(
            detailBuku = detailBuku,
            isEntryValid = validasiInput(detailBuku)
        )
    }

    suspend fun saveBuku(): Boolean {
        return if (validasiInput()) {
            try {
                repositori.insertBuku(
                    uiStateBuku.detailBuku.toBuku(),
                    uiStateBuku.detailBuku.pengarangIds
                )
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else false
    }
}
