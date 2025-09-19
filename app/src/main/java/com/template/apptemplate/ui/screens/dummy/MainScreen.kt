package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import android.location.Location
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.template.apptemplate.ui.ui.theme.AppTemplateTheme

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current

    var selectedLocationTarget by remember { mutableStateOf<LocationTarget?>(null) }

    var distanceGNSS by remember { mutableStateOf<Float?>(null) }
    var distanceGPS by remember { mutableStateOf<Float?>(null) }

    var gpsLocation by remember { mutableStateOf<Location?>(null) }

    val locationHelper = remember { LocationHelper(context = context) }

    var gnssData by remember { mutableStateOf(GNSSData(null, 0)) }

    GNSS.init(context) {
        gnssData = it
    }

    LocationPermissionHelper(
        onGranted = {
            locationHelper.getLocationCallback(onUpdated = { location ->
                gpsLocation = location
            })
            GNSS.start()
        },
    )

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
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    var isCustomLocationSelected by remember { mutableStateOf(false) }
    var customLocationValue by remember { mutableStateOf(TextFieldValue()) }

    val onSaveValue: () -> Unit = {
        runCatching {
            val value = customLocationValue.text.split(",")

            val lat = value[0].toDoubleOrNull() ?: 0.0
            val lng = value[1].toDoubleOrNull() ?: 0.0
            val locationTarget = LocationTarget("Custom", lat, lng)

            onSelectedLocationTarget.invoke(locationTarget)
            isCustomLocationSelected = false
        }.onFailure {
            Toast.makeText(context, "Format Lokasi ada yg salah", Toast.LENGTH_SHORT).show()
        }
    }

    if (isCustomLocationSelected) {
        Dialog(onDismissRequest = { isCustomLocationSelected = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Masukkan Latitude dan Longitude\nContoh: -6.199411, 106.821869")

                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        keyboardActions = KeyboardActions(
                            onDone = { onSaveValue.invoke() }
                        ),
                        singleLine = true,
                        placeholder = { Text(text = "Masukkan disini") },
                        trailingIcon = {
                            if (customLocationValue.text.isNotEmpty()) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        customLocationValue = customLocationValue.copy(text = "")
                                    },
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = ""
                                )
                            }
                        },
                        value = customLocationValue,
                        onValueChange = { customLocationValue = it }
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            enabled = customLocationValue.text.isNotEmpty(),
                            onClick = { onSaveValue.invoke() }
                        ) {
                            Text(text = "Simpan")
                        }
                    }
                }
            }
        }
    }

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
                        if (item.lat == 0.0 && item.lng == 0.0) {
                            isCustomLocationSelected = true
                        } else {
                            onSelectedLocationTarget.invoke(item)
                        }
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
private fun ItemViewPreview() {
    AppTemplateTheme {
        ItemView("Android")
    }
}
