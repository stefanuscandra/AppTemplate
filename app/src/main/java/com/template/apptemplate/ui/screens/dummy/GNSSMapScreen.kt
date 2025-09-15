package com.template.apptemplate.ui.screens.dummy

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun GNSSMapScreen(
    modifier: Modifier = Modifier,
    gnssData: GNSSData,
    locationTarget: LocationTarget? = null,
    gpsLocation: Location? = null,
    onDistanceGNSSChanged: (Float) -> Unit = {},
    onDistanceGPSChanged: (Float) -> Unit = {},
) {

    val targetLocation by remember(locationTarget) {
        val target = locationTarget?.run { LatLng(lat, lng) }
        mutableStateOf(target)
    }
    val coroutineScope = rememberCoroutineScope()

    // default location (indonesia)
    var defaultLatLng by remember {
        val loc = LatLng(-6.2293796, 106.6647042)
        mutableStateOf(loc)
    }

    var gnssLocation by remember(gnssData.location) { mutableStateOf(gnssData.location) }
    val cameraPositionState = rememberCameraPositionState(
        init = {
            coroutineScope.launch {
                animate(
                    update = CameraUpdateFactory.newLatLng(defaultLatLng),
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
            targetLocation?.let { target ->
                Marker(
                    state = MarkerState(position = target),
                    title = "Target Location",
                    snippet = "This is the target location",
                    icon = remember { BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) }
                )
            }

            gpsLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)

                targetLocation?.let { target ->
                    val distance = LocationHelper.calculateDistanceInMeters(
                        startLocation = latLng,
                        endLocation = target
                    )
                    onDistanceGPSChanged.invoke(distance)

                    Polyline(
                        points = listOf(latLng, target),
                        color = Color.Magenta,
                        width = 8f
                    )
                }

                Marker(
                    state = MarkerState(position = latLng),
                    title = "Gps Location",
                    snippet = "This is from gps location",
                    icon = remember { BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA) }
                )
            }

            gnssLocation?.let { loc ->
                val latLng = LatLng(loc.latitude, loc.longitude)

                targetLocation?.let { target ->
                    val distance = LocationHelper.calculateDistanceInMeters(
                        startLocation = latLng,
                        endLocation = target
                    )
                    onDistanceGNSSChanged.invoke(distance)

                    Polyline(
                        points = listOf(latLng, target),
                        color = Color.Red,
                        width = 8f
                    )
                }

                LaunchedEffect(latLng) {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newLatLng(latLng),
                        durationMs = 1000
                    )
                }

                Marker(
                    state = MarkerState(position = latLng),
                    title = "This is from GNSS location",
                    snippet = "Satellites used: ${gnssData.usedSatellites}"
                )
            }
        }
    }
}
