package com.example.remedialucp2.repositori

import android.app.Application
import android.content.Context
import com.example.remedialucp2.room.DatabasePerpustakaan

interface ContainerApp {
    val repositoriPerpustakaan: RepositoriPerpustakaan
}

class ContainerDataApp(private val context: Context) : ContainerApp {
    private val database = DatabasePerpustakaan.getDatabase(context)

    override val repositoriPerpustakaan: RepositoriPerpustakaan by lazy {
        RepositoriPerpustakaan(
            bukuDao = database.bukuDao(),
            pengarangDao = database.pengarangDao(),
            kategoriDao = database.kategoriDao(),
            bukuPengarangDao = database.bukuPengarangDao(),
            database = database
        )
    }
}

class AplikasiPerpustakaan : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = ContainerDataApp(this)
    }
}
