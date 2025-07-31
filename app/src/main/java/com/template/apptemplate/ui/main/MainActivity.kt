package com.template.apptemplate.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.template.apptemplate.ui.AppNavHost
import com.template.apptemplate.ui.ui.theme.AppTemplateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTemplateTheme {
                AppNavHost(
                    navController = rememberNavController()
                )
            }
        }
    }
}
