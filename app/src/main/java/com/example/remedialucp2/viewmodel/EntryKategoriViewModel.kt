package com.example.remedialucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.remedialucp2.modeldata.DetailKategori
import com.example.remedialucp2.modeldata.UIStateKategori
import com.example.remedialucp2.modeldata.toKategori
import com.example.remedialucp2.repositori.RepositoriPerpustakaan

class EntryKategoriViewModel(private val repositori: RepositoriPerpustakaan) : ViewModel() {
    var uiStateKategori by mutableStateOf(UIStateKategori())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun validasiInput(uiState: DetailKategori = uiStateKategori.detailKategori): Boolean {
        return uiState.nama.isNotBlank()
    }

    fun updateUiState(detailKategori: DetailKategori) {
        uiStateKategori = UIStateKategori(
            detailKategori = detailKategori,
            isEntryValid = validasiInput(detailKategori)
        )
        errorMessage = null
    }

    suspend fun saveKategori(): Boolean {
        return if (validasiInput()) {
            try {
                repositori.insertKategori(uiStateKategori.detailKategori.toKategori())
                true
            } catch (e: IllegalArgumentException) {
                errorMessage = e.message // Cyclic reference error
                false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else false
    }
}
