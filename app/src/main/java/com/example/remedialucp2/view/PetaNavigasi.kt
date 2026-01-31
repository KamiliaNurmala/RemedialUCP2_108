package com.example.remedialucp2.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.remedialucp2.view.route.*

@Composable
fun PerpustakaanApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = modifier
    ) {
        // Home
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToEntryBuku = { navController.navigate(DestinasiEntryBuku.route) },
                navigateToEntryPengarang = { navController.navigate(DestinasiEntryPengarang.route) },
                navigateToEntryKategori = { navController.navigate(DestinasiEntryKategori.route) },
                navigateToDetailBuku = { navController.navigate("${DestinasiDetailBuku.route}/$it") },
                navigateToDetailPengarang = { navController.navigate("${DestinasiDetailPengarang.route}/$it") },
                navigateToDetailKategori = { navController.navigate("${DestinasiDetailKategori.route}/$it") }
            )
        }

        // Entry Buku
        composable(DestinasiEntryBuku.route) {
            EntryBukuScreen(navigateBack = { navController.popBackStack() })
        }

        // Entry Pengarang
        composable(DestinasiEntryPengarang.route) {
            EntryPengarangScreen(navigateBack = { navController.popBackStack() })
        }

        // Entry Kategori
        composable(DestinasiEntryKategori.route) {
            EntryKategoriScreen(navigateBack = { navController.popBackStack() })
        }

        // Detail Buku
        composable(
            route = DestinasiDetailBuku.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailBuku.bukuIdArg) {
                type = NavType.IntType
            })
        ) {
            DetailBukuScreen(navigateBack = { navController.popBackStack() })
        }

        // Detail Pengarang
        composable(
            route = DestinasiDetailPengarang.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailPengarang.pengarangIdArg) {
                type = NavType.IntType
            })
        ) {
        }

        // Detail Kategori
        composable(
            route = DestinasiDetailKategori.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailKategori.kategoriIdArg) {
                type = NavType.IntType
            })
        ) {
        }
    }
}
