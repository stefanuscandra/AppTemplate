package com.template.apptemplate.ui.screens.dummy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.template.apptemplate.ui.NavigationItem
import com.template.apptemplate.ui.ui.theme.AppTemplateTheme

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val data by viewModel.data.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            data.forEach { item ->
                ItemView(
                    id = item.stationId,
                    onClick = {
                        val route = NavigationItem.Detail.route.replace("{id}", item.stationId)
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}

@Composable
private fun ItemView(id: String, onClick: (String) -> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable {
            onClick.invoke(id)
        }) {
        Text(text = "Station ID : $id!")
        HorizontalDivider()
    }
}


@Preview(showBackground = true)
@Composable
fun ItemViewPreview() {
    AppTemplateTheme {
        ItemView("Android")
    }
}
