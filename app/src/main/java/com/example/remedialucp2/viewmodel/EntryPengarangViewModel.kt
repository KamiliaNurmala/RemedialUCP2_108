package com.example.remedialucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.remedialucp2.modeldata.DetailPengarang
import com.example.remedialucp2.modeldata.UIStatePengarang
import com.example.remedialucp2.modeldata.toPengarang
import com.example.remedialucp2.repositori.RepositoriPerpustakaan

class EntryPengarangViewModel(private val repositori: RepositoriPerpustakaan) : ViewModel() {
    var uiStatePengarang by mutableStateOf(UIStatePengarang())
        private set

    private fun validasiInput(uiState: DetailPengarang = uiStatePengarang.detailPengarang): Boolean {
        return with(uiState) {
            nama.isNotBlank() && negara.isNotBlank()
        }
    }

    fun updateUiState(detailPengarang: DetailPengarang) {
        uiStatePengarang = UIStatePengarang(
            detailPengarang = detailPengarang,
            isEntryValid = validasiInput(detailPengarang)
        )
    }

    suspend fun savePengarang(): Boolean {
        return if (validasiInput()) {
            try {
                repositori.insertPengarang(uiStatePengarang.detailPengarang.toPengarang())
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else false
    }
}
