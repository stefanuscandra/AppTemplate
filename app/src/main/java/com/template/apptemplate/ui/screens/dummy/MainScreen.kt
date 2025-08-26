package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.template.apptemplate.ui.ui.theme.AppTemplateTheme

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val data by viewModel.data.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var gnssData by remember { mutableStateOf(GNSSData(null, 0)) }

    GNSS.init(context) {
        gnssData = it
    }

    LaunchedEffect(Unit) {
        GNSS.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            GNSS.stop()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Satellites used in fix : ${gnssData.usedSatellites.takeIf { it != 0 } ?: '-'}"
            )
            ItemView("Lat : ${gnssData.location?.latitude ?: '-'}")
            ItemView("Long : ${gnssData.location?.longitude ?: '-'}")
            ItemView("Acc : ${gnssData.location?.accuracy ?: '-'}")
            GNSSMapScreen(
                modifier = Modifier.fillMaxHeight(),
                gnssData = gnssData,
            )
        }
    }
}

@Composable
private fun ItemView(id: String, onClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick.invoke(id)
            }) {
        Text(text = "location: $id")
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
