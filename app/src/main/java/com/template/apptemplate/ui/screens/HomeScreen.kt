@file:OptIn(ExperimentalMaterial3Api::class)

package com.template.apptemplate.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.template.apptemplate.data.network.response.readings.ReadingItem
import com.template.apptemplate.ui.NavigationItem

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    println("temperature : ${state.readings}")

    HomeContent(
        state = state,
        action = viewModel::onAction,
        navController = navController
    )
}

@Composable
private fun HomeContent(
    state: HomeState = HomeState(),
    action: (HomeAction) -> Unit,
    navController: NavController? = null
) {
    Scaffold { pad ->
        LazyColumn(modifier = Modifier.padding(pad)) {
            items(state.readings) { item ->
                ReadingItemView(item) {
                    navController?.navigate(
                        NavigationItem.Detail.withArgs(
                            item.stationId,
                            item.value.toString()
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ReadingItemView(item: ReadingItem, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick.invoke() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Station ID -> ${item.stationId}")
        Text("Value -> ${item.value}")
    }
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    val state = HomeState().apply {
        readings = listOf(
            ReadingItem(
                "123", 2.1
            ),
            ReadingItem(
                "123", 2.1
            )
        )
    }

    HomeContent(state = state, action = {})
}
