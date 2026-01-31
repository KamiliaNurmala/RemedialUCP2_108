package com.example.remedialucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.modeldata.DetailBuku
import com.example.remedialucp2.modeldata.toDetailBuku
import com.example.remedialucp2.modeldata.UIStateBuku
import com.example.remedialucp2.modeldata.toBuku
import com.example.remedialucp2.repositori.RepositoriPerpustakaan
import com.example.remedialucp2.room.Buku
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositori: RepositoriPerpustakaan
) : ViewModel() {
    private val bukuId: Int = checkNotNull(savedStateHandle["bukuId"])

    var uiState by mutableStateOf(UIStateBuku())
        private set

    init {
        viewModelScope.launch {
            val buku = repositori.getBukuById(bukuId).filterNotNull().first()
            val pengarangRelasi = repositori.getPengarangByBuku(bukuId).first()
            uiState = UIStateBuku(
                detailBuku = buku.toDetailBuku().copy(
                    pengarangIds = pengarangRelasi.map { it.pengarangId }
                ),
                isEntryValid = true
            )
        }
    }

    suspend fun updateBuku(): Boolean {
        return try {
            repositori.updateBuku(
                uiState.detailBuku.toBuku(),
                uiState.detailBuku.pengarangIds
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteBuku() {
        repositori.softDeleteBuku(bukuId)
    }

    suspend fun toggleStatusPinjam() {
        val currentBuku = repositori.getBukuById(bukuId).filterNotNull().first()
        val updatedBuku = currentBuku.copy(statusPinjam = !currentBuku.statusPinjam)
        repositori.updateBuku(updatedBuku, uiState.detailBuku.pengarangIds)
        uiState = uiState.copy(
            detailBuku = uiState.detailBuku.copy(statusPinjam = updatedBuku.statusPinjam)
        )
    }
}
