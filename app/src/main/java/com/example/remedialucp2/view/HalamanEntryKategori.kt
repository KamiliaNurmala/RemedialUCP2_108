package com.example.remedialucp2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.remedialucp2.R
import com.example.remedialucp2.room.Kategori
import com.example.remedialucp2.viewmodel.EntryKategoriViewModel
import com.example.remedialucp2.viewmodel.HomeViewModel
import com.example.remedialucp2.viewmodel.HomeUiState
import com.example.remedialucp2.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryKategoriScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory),
    homeViewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBarPerpustakaan(
                title = stringResource(R.string.tambah_kategori),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = viewModel.uiStateKategori.detailKategori.nama,
                onValueChange = {
                    viewModel.updateUiState(viewModel.uiStateKategori.detailKategori.copy(nama = it))
                },
                label = { Text(stringResource(R.string.nama_kategori)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Parent Kategori Dropdown
            var expanded by remember { mutableStateOf(false) }
            val kategoriList = if (homeViewModel.kategoriUiState is HomeUiState.Success) {
                (homeViewModel.kategoriUiState as HomeUiState.Success<Kategori>).data
            } else emptyList()
            val selectedParent = kategoriList.find { it.id == viewModel.uiStateKategori.detailKategori.parentId }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = selectedParent?.nama ?: "Root (Tanpa Induk)",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.parent_kategori)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Root (Tanpa Induk)") },
                        onClick = {
                            viewModel.updateUiState(viewModel.uiStateKategori.detailKategori.copy(parentId = null))
                            expanded = false
                        }
                    )
                    kategoriList.forEach { kategori ->
                        DropdownMenuItem(
                            text = { Text(kategori.nama) },
                            onClick = {
                                viewModel.updateUiState(viewModel.uiStateKategori.detailKategori.copy(parentId = kategori.id))
                                expanded = false
                            }
                        )
                    }
                }
            }

            viewModel.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = stringResource(R.string.required_field),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.saveKategori()) {
                            navigateBack()
                        }
                    }
                },
                enabled = viewModel.uiStateKategori.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_submit))
            }
        }
    }
}
