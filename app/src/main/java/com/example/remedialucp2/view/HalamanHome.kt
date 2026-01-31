package com.example.remedialucp2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.room.Buku
import com.example.remedialucp2.room.Pengarang
import com.example.remedialucp2.room.Kategori
import com.example.remedialucp2.viewmodel.HomeViewModel
import com.example.remedialucp2.viewmodel.HomeUiState
import com.example.remedialucp2.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEntryBuku: () -> Unit,
    navigateToEntryPengarang: () -> Unit,
    navigateToEntryKategori: () -> Unit,
    navigateToDetailBuku: (Int) -> Unit,
    navigateToDetailPengarang: (Int) -> Unit,
    navigateToDetailKategori: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.tab_buku),
        stringResource(R.string.tab_pengarang),
        stringResource(R.string.tab_kategori)
    )

    Scaffold(
        topBar = {
            TopAppBarPerpustakaan(
                title = stringResource(R.string.app_name),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (selectedTab) {
                        0 -> navigateToEntryBuku()
                        1 -> navigateToEntryPengarang()
                        2 -> navigateToEntryKategori()
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Book, contentDescription = null)
                                1 -> Icon(Icons.Default.Person, contentDescription = null)
                                2 -> Icon(Icons.Default.Category, contentDescription = null)
                            }
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> BukuContent(
                    uiState = viewModel.bukuUiState,
                    onItemClick = navigateToDetailBuku,
                    onRetry = { viewModel.loadAllData() }
                )
                1 -> PengarangContent(
                    uiState = viewModel.pengarangUiState,
                    onItemClick = navigateToDetailPengarang,
                    onRetry = { viewModel.loadAllData() }
                )
                2 -> KategoriContent(
                    uiState = viewModel.kategoriUiState,
                    onItemClick = navigateToDetailKategori,
                    onRetry = { viewModel.loadAllData() }
                )
            }
        }
    }
}

@Composable
fun BukuContent(
    uiState: HomeUiState<Buku>,
    onItemClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Error -> ErrorScreen(onRetry)
        is HomeUiState.Success -> {
            if (uiState.data.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data buku")
                }
            } else {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(items = uiState.data, key = { it.id }) { buku ->
                        BukuItem(buku = buku, onClick = { onItemClick(buku.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun BukuItem(buku: Buku, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = buku.judul, style = MaterialTheme.typography.titleMedium)
            Text(text = "ISBN: ${buku.isbn}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (buku.statusPinjam) "Dipinjam" else "Tersedia",
                style = MaterialTheme.typography.bodySmall,
                color = if (buku.statusPinjam) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PengarangContent(
    uiState: HomeUiState<Pengarang>,
    onItemClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Error -> ErrorScreen(onRetry)
        is HomeUiState.Success -> {
            if (uiState.data.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data pengarang")
                }
            } else {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(items = uiState.data, key = { it.id }) { pengarang ->
                        PengarangItem(pengarang = pengarang, onClick = { onItemClick(pengarang.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun PengarangItem(pengarang: Pengarang, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = pengarang.nama, style = MaterialTheme.typography.titleMedium)
            Text(text = pengarang.negara, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun KategoriContent(
    uiState: HomeUiState<Kategori>,
    onItemClick: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Error -> ErrorScreen(onRetry)
        is HomeUiState.Success -> {
            if (uiState.data.isEmpty()) {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada data kategori")
                }
            } else {
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(items = uiState.data, key = { it.id }) { kategori ->
                        KategoriItem(kategori = kategori, onClick = { onItemClick(kategori.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun KategoriItem(kategori: Kategori, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_small))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(text = kategori.nama, style = MaterialTheme.typography.titleMedium)
            if (kategori.parentId != null) {
                Text(text = "Sub-kategori", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.error), modifier = Modifier.padding(16.dp))
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}
