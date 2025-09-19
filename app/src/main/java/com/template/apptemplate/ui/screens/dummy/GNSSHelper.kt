package com.template.apptemplate.ui.screens.dummy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

data class GNSSData(
    val location: Location?,
    val usedSatellites: Int,
)

object GNSS {
    private var appContext: Context? = null
    private var locationManager: LocationManager? = null
    private var callbackRef: WeakReference<(GNSSData) -> Unit>? = null
    private var usedSatellites: Int = 0

    private val gnssCallback = object : GnssStatus.Callback() {
        override fun onSatelliteStatusChanged(status: GnssStatus) {
            usedSatellites = (0 until status.satelliteCount).count { status.usedInFix(it) }
            if (usedSatellites > 5) {
                callbackRef?.get()?.invoke(GNSSData(null, usedSatellites))
            } else {
                Log.d("GNSS", "Satellites used: $usedSatellites")
            }
        }
    }

    private val locationListener = LocationListener { location ->
        if (location.accuracy <= 10) {
            Log.d("GNSS", "accurate: ${location.latitude}, ${location.longitude}, acc=${location.accuracy}")
            callbackRef?.get()?.invoke(GNSSData(location, usedSatellites))
        } else {
            Log.d("GNSS", "inaccurate: ${location.latitude}, ${location.longitude}, acc=${location.accuracy}")
        }
    }

    fun init(ctx: Context, callback: (GNSSData) -> Unit) {
        appContext = ctx.applicationContext
        locationManager = appContext?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        callbackRef = WeakReference(callback)
    }

    fun start() {
        val lm = locationManager ?: return
        val context = appContext ?: return

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                lm.registerGnssStatusCallback(ContextCompat.getMainExecutor(context), gnssCallback)
            } else {
                lm.registerGnssStatusCallback(gnssCallback)
            }

            lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                0f,
                locationListener,
                Looper.getMainLooper()
            )
        } else {
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
        }
    }

    fun stop() {
        locationManager?.let { lm ->
            lm.removeUpdates(locationListener)
            lm.unregisterGnssStatusCallback(gnssCallback)
        }
        callbackRef?.clear()
    }
}
