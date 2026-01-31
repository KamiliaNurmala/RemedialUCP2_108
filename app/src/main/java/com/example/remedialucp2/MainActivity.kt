package com.example.remedialucp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.remedialucp2.ui.theme.RemedialUCP2Theme
import com.example.remedialucp2.view.PerpustakaanApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RemedialUCP2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PerpustakaanApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}