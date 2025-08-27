package com.template.apptemplate.ui.screens.dummy

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun GNSSMapScreen(
    modifier: Modifier = Modifier,
    gnssData: GNSSData,
    gpsLocation: Location? = null,
    onDistanceGNSSChanged: (Float) -> Unit = {},
    onDistanceGPSChanged: (Float) -> Unit = {}
) {

    val targetLocation = LatLng(-6.199411, 106.821869)
    val coroutineScope = rememberCoroutineScope()

    var zoomLevel by remember { mutableFloatStateOf(4f) }

    // default location (indonesia)
    var defaultLatLng by remember {
        val loc = LatLng(-0.7094314, 112.3666611)
        mutableStateOf(loc)
    }

    var gnssLocation by remember(gnssData.location) { mutableStateOf(gnssData.location) }
    val cameraPositionState = rememberCameraPositionState(
        init = {
            coroutineScope.launch {
                animate(
                    update = CameraUpdateFactory.newLatLngZoom(defaultLatLng, zoomLevel),
                    durationMs = 1000
                )
            }
        }
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            Marker(
                state = rememberMarkerState(position = targetLocation),
                title = "Target Location",
                snippet = "This is the target location",
                icon = remember { BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) }
            )

            gpsLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)

                val distance = LocationHelper.calculateDistanceInMeters(
                    startLocation = latLng,
                    endLocation = targetLocation
                )
                onDistanceGPSChanged.invoke(distance)

                Marker(
                    state = rememberMarkerState(position = latLng),
                    title = "Gps Location",
                    snippet = "This is from gps location",
                    icon = remember { BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA) }
                )

                Polyline(
                    points = listOf(latLng, targetLocation),
                    color = Color.Magenta,
                    width = 8f
                )
            }

            gnssLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)

                val distance = LocationHelper.calculateDistanceInMeters(
                    startLocation = latLng,
                    endLocation = targetLocation
                )
                onDistanceGNSSChanged.invoke(distance)

                LaunchedEffect(latLng) {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel),
                        durationMs = 1000
                    )
                }

                LaunchedEffect(Unit) {
                    snapshotFlow { cameraPositionState.position.zoom }
                        .collect { currentZoom ->
                            zoomLevel = currentZoom
                        }
                }

                Marker(
                    state = rememberMarkerState(position = latLng),
                    title = "This is from GNSS location",
                    snippet = "Satellites used: ${gnssData.usedSatellites}"
                )

                Polyline(
                    points = listOf(latLng, targetLocation),
                    color = Color.Red,
                    width = 8f
                )
            }
        }
    }
}
