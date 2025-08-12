@file:OptIn(ExperimentalMaterial3Api::class)

package com.template.apptemplate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController) {
    val backStackEntry = remember { navController.currentBackStackEntry }
    val id by remember {
        mutableStateOf(
            backStackEntry?.arguments?.getString("id").orEmpty()
        )
    }
    val value by remember {
        mutableStateOf(
            backStackEntry?.arguments?.getString("value").orEmpty()
        )
    }

    DetailContent(id, value, navController)
}

@Composable
private fun DetailContent(
    id: String,
    value: String,
    navController: NavController? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Temperature") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable { navController?.popBackStack() },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                },
            )
        }
    ) { pad ->
        Column(
            Modifier
                .padding(pad)
                .background(color = Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val idText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold)
                ) { append("Station ID : ") }
                append(id)
            }
            val valueText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                ) { append("Value : ") }
                append(value)
            }
            Text(text = idText)
            Text(text = valueText)
        }
    }
}

@Preview
@Composable
private fun DetailContentPreview() {
    DetailContent(
        "123",
        "1.2"
    )
}