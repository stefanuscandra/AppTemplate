package com.template.apptemplate.ui.screens.dummy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GNSSMapScreen(
    gnssData: GNSSData,
) {
    var lastLocation by remember { mutableStateOf(gnssData.location) }

    val cameraPositionState = rememberCameraPositionState()
    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            lastLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)
                LaunchedEffect(latLng) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(latLng, 16f),
                        durationMs = 1000
                    )
                }
                Marker(
                    state = MarkerState(position = latLng),
                    title = "You are here",
                    snippet = "Satellites used: ${gnssData.usedSatellites}"
                )
            }
        }
    }
}
