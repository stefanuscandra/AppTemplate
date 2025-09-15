package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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

    var selectedLocationTarget by remember { mutableStateOf<LocationTarget?>(null) }

    var distanceGNSS by remember { mutableStateOf<Float?>(null) }
    var distanceGPS by remember { mutableStateOf<Float?>(null) }

    var gpsLocation by remember { mutableStateOf<Location?>(null) }

    val locationHelper = remember { LocationHelper(context = context) }
    locationHelper.getLocationCallback(onUpdated = { location ->
        gpsLocation = location
    })

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
            ItemView("Acc : ${gnssData.location?.accuracy ?: '-'}")
            ItemView("GNSS Location : \n${gnssData.location?.latitude ?: '-'}, ${gnssData.location?.longitude ?: '-'}")
            ItemView("GPS Location : \n${gpsLocation?.latitude ?: '-'}, ${gpsLocation?.longitude ?: '-'}")

            TargetLocationPicker(
                locationTargets = LocationTarget.getList(),
                selectedLocationTarget = selectedLocationTarget,
                onSelectedLocationTarget = { target ->
                    selectedLocationTarget = target
                }
            )
            Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp), text = "Distance to Target")
            ItemView("From GNSS : ${distanceGNSS ?: ' '} m")
            ItemView("From GPS : ${distanceGPS ?: ' '} m")

            GNSSMapScreen(
                modifier = Modifier.fillMaxHeight(),
                gnssData = gnssData,
                gpsLocation = gpsLocation,
                locationTarget = selectedLocationTarget,
                onDistanceGNSSChanged = {
                    distanceGNSS = it
                },
                onDistanceGPSChanged = {
                    distanceGPS = it
                }
            )
        }
    }
}

@Composable
private fun TargetLocationPicker(
    locationTargets: List<LocationTarget>,
    modifier: Modifier = Modifier,
    selectedLocationTarget: LocationTarget? = null,
    onSelectedLocationTarget: (LocationTarget) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column {
            Text(text = "Lokasi Target")
            Spacer(modifier = Modifier.size(4.dp))

            Column(
                modifier = Modifier.clickable { expanded = !expanded }
            ) {
                val color = Color.Black
                    .takeIf { selectedLocationTarget?.name?.isNotEmpty() ?: false }
                    ?: Color.Black.copy(alpha = .4f)

                val text = selectedLocationTarget?.name.orEmpty()
                    .takeIf { it.isNotEmpty() }
                    ?: "Pilih Lokasi Target"

                Text(text = text, color = color)
                Spacer(modifier = Modifier.size(4.dp))
                HorizontalDivider()
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            locationTargets.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelectedLocationTarget.invoke(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TargetLocationPickerPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        TargetLocationPicker(locationTargets = LocationTarget.getList())
    }
}

@Composable
private fun ItemView(title: String, onClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick.invoke(title) }
    ) {
        SelectionContainer { Text(text = title, overflow = TextOverflow.Ellipsis, maxLines = 2) }
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
