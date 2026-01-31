package com.example.remedialucp2.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.remedialucp2.repositori.AplikasiPerpustakaan

fun CreationExtras.aplikasiPerpustakaan(): AplikasiPerpustakaan = (
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiPerpustakaan
)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(aplikasiPerpustakaan().container.repositoriPerpustakaan)
        }

        initializer {
            EntryBukuViewModel(aplikasiPerpustakaan().container.repositoriPerpustakaan)
        }

        initializer {
            EntryPengarangViewModel(aplikasiPerpustakaan().container.repositoriPerpustakaan)
        }

        initializer {
            EntryKategoriViewModel(aplikasiPerpustakaan().container.repositoriPerpustakaan)
        }

        initializer {
            DetailBukuViewModel(
                this.createSavedStateHandle(),
                aplikasiPerpustakaan().container.repositoriPerpustakaan
            )
        }

//        initializer {
//            DetailPengarangViewModel(
//                this.createSavedStateHandle(),
//                aplikasiPerpustakaan().container.repositoriPerpustakaan
//            )
//        }
//
//        initializer {
//            DetailKategoriViewModel(
//                this.createSavedStateHandle(),
//                aplikasiPerpustakaan().container.repositoriPerpustakaan
//            )
//        }
    }
}
