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
import com.example.remedialucp2.modeldata.DetailPengarang
import com.example.remedialucp2.viewmodel.EntryPengarangViewModel
import com.example.remedialucp2.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPengarangScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryPengarangViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBarPerpustakaan(
                title = stringResource(R.string.tambah_pengarang),
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
                value = viewModel.uiStatePengarang.detailPengarang.nama,
                onValueChange = {
                    viewModel.updateUiState(viewModel.uiStatePengarang.detailPengarang.copy(nama = it))
                },
                label = { Text(stringResource(R.string.nama)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = viewModel.uiStatePengarang.detailPengarang.negara,
                onValueChange = {
                    viewModel.updateUiState(viewModel.uiStatePengarang.detailPengarang.copy(negara = it))
                },
                label = { Text(stringResource(R.string.negara)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text(
                text = stringResource(R.string.required_field),
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.savePengarang()) {
                            navigateBack()
                        }
                    }
                },
                enabled = viewModel.uiStatePengarang.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_submit))
            }
        }
    }
}
