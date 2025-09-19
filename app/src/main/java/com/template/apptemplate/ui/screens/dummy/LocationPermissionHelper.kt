package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHelper(
    onGranted: () -> Unit,
    onChangeLocation: (Location) -> Unit = {},
) {
    val context = LocalContext.current

    val locationHelper = remember { LocationHelper(context) }

    val changeLocation = { location: Location? ->
        location?.let { onChangeLocation(it) }
    }

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = {
            if (it) onGranted()
            locationHelper.getLocation { location ->
                changeLocation(location)
            }
        }
    )

    DisposableEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            onGranted()
            locationHelper.getLocationCallback { location ->
                changeLocation(location)
            }
        } else {
            locationPermissionState.launchPermissionRequest()
        }

        onDispose { locationHelper.stopLocationUpdates() }
    }
}
