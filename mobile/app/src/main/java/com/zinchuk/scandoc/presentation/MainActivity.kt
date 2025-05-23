package com.zinchuk.scandoc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.zinchuk.scandoc.presentation.navigation.NavigationHost
import com.zinchuk.scandoc.presentation.theme.ScanDocTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initContent()
    }

    private fun initContent() {
        enableEdgeToEdge()
        setContent {
            val controller = rememberNavController()
            ScanDocTheme {
                NavigationHost(controller)
            }
        }
    }
}
