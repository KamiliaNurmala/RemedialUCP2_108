package com.example.remedialucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remedialucp2.repositori.RepositoriPerpustakaan
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.room.Kategori
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState<T> {
    class Loading<T> : HomeUiState<T>
    data class Success<T>(val data: List<T>) : HomeUiState<T>
    class Error<T> : HomeUiState<T>
}

class HomeViewModel(private val repositori: RepositoriPerpustakaan) : ViewModel() {
    var bukuUiState: HomeUiState<Buku> by mutableStateOf(HomeUiState.Loading())
        private set

    var pengarangUiState: HomeUiState<Pengarang> by mutableStateOf(HomeUiState.Loading())
        private set

    var kategoriUiState: HomeUiState<Kategori> by mutableStateOf(HomeUiState.Loading())
        private set

    init {
        loadAllData()
    }

    fun loadAllData() {
        loadBuku()
        loadPengarang()
        loadKategori()
    }

    private fun loadBuku() {
        viewModelScope.launch {
            bukuUiState = HomeUiState.Loading()
            try {
                repositori.getAllBuku().collect { list ->
                    bukuUiState = HomeUiState.Success(list)
                }
            } catch (e: Exception) {
                bukuUiState = HomeUiState.Error()
            }
        }
    }

    private fun loadPengarang() {
        viewModelScope.launch {
            pengarangUiState = HomeUiState.Loading()
            try {
                repositori.getAllPengarang().collect { list ->
                    pengarangUiState = HomeUiState.Success(list)
                }
            } catch (e: Exception) {
                pengarangUiState = HomeUiState.Error()
            }
        }
    }

    private fun loadKategori() {
        viewModelScope.launch {
            kategoriUiState = HomeUiState.Loading()
            try {
                repositori.getAllKategori().collect { list ->
                    kategoriUiState = HomeUiState.Success(list)
                }
            } catch (e: Exception) {
                kategoriUiState = HomeUiState.Error()
            }
        }
    }
}
