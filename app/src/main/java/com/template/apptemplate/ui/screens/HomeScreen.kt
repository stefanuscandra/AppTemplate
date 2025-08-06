package com.template.apptemplate.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        action = viewModel::onAction
    )
}

@Composable
private fun HomeContent(
    state: HomeState = HomeState(),
    action: (HomeAction) -> Unit
) {
    //TODO make your magic here
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    val state = HomeState()

    HomeContent(state = state, action = {})
}
