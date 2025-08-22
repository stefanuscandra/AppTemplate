package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import android.R.attr.priority
import android.content.Context
import android.location.GnssStatus
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.renderscript.RenderScript
import android.util.Log
import androidx.annotation.RequiresPermission
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.template.apptemplate.ui.NavigationItem
import com.template.apptemplate.ui.ui.theme.AppTemplateTheme

@RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val data by viewModel.data.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // check location permission first

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        println("GNSS cek register >= Build.VERSION_CODES.R")
        locationManager.registerGnssStatusCallback(
            ContextCompat.getMainExecutor(context),
            object : GnssStatus.Callback() {
                override fun onStarted() {
                    println("GNSS cek started")
                }

                override fun onStopped() {
                    println("GNSS cek stopped")
                }

                override fun onFirstFix(ttffMillis: Int) {
                    println("GNSS cek first fix : $ttffMillis")
                }

                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    val usedSatellites = (0 until status.satelliteCount).count { status.usedInFix(it) }
                    Log.d("GNSS", "Satellites used in fix: $usedSatellites")
                    val location = Location(locationManager.allProviders.first())

                    if (location.accuracy <= 10 && usedSatellites >= 4) {
                        println("cek location: ${location.latitude}, ${location.longitude}")
                    } else {
                        println("cek waiting location: ${location.latitude}, ${location.longitude}")
                    }
                }
            })
    } else {
        println("GNSS cek register < Build.VERSION_CODES.R")
        @Suppress("DEPRECATION")
        locationManager.registerGnssStatusCallback(
            object : GnssStatus.Callback() {
                override fun onStarted() {
                    println("GNSS cek started")
                }

                override fun onStopped() {
                    println("GNSS cek stopped")
                }

                override fun onFirstFix(ttffMillis: Int) {
                    println("GNSS cek first fix : $ttffMillis")
                }

                override fun onSatelliteStatusChanged(status: GnssStatus) {
                    val usedSatellites = (0 until status.satelliteCount).count { status.usedInFix(it) }
                    Log.d("GNSS", "Satellites used in fix: $usedSatellites")
                    val location = Location(locationManager.allProviders.first())

                    if (location.accuracy <= 10 && usedSatellites >= 4) {
                        println("cek location: ${location.latitude}, ${location.longitude}")
                    } else {
                        println("cek waiting location: ${location.latitude}, ${location.longitude}")
                    }
                }
            })
    }

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
    Column(
        modifier = Modifier
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
