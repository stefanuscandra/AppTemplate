package com.template.apptemplate.ui.screens.dummy

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.Global.AIRPLANE_MODE_ON
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
class LocationHelper(private val context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    fun getLocation(
        onLocationGenerated: (Location?) -> Unit,
    ) {
        checkLocationAvailability()
        checkLocationPermissions()
        checkBatterySaverMode()
        checkAirplaneModeOn()

        checkGps(
            context = context, onGpsEnabled = {
                getCurrentLocation(onLocationGenerated)
            }
        )
    }

    fun getCurrentLocation(
        onLocationGenerated: (Location?) -> Unit,
    ) {
        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location == null)
                    getLastLocation(onLocationGenerated = {
                        onLocationGenerated.invoke(it)
                    })
                else
                    onLocationGenerated.invoke(location)
            }.addOnFailureListener {
                recordError("getCurrentLocation", it)
                getLastLocation(onLocationGenerated)
            }
    }

    fun getLastLocation(
        onLocationGenerated: (Location?) -> Unit,
        shouldRecordException: Boolean = true,
    ) {

        client.lastLocation.addOnSuccessListener { location ->
            onLocationGenerated.invoke(location)
        }

        client.lastLocation.addOnFailureListener {
            if (shouldRecordException) recordError("getLastLocation", it)
            onLocationGenerated.invoke(null)
        }
    }

    private fun checkGps(context: Context, onGpsEnabled: () -> Unit) {
        runCatching {
            val locationManager =
                context.getSystemService(LOCATION_SERVICE) as LocationManager

            val gpsEnabled = locationManager.isProviderEnabled(GPS_PROVIDER)

            if (gpsEnabled)
                onGpsEnabled.invoke()
            else {
                recordError("checkGps", Exception("Gps is not enabled"))
            }
        }.onFailure {
            recordError("checkGps", it)
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10).apply {
            setMinUpdateDistanceMeters(1f)
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            setGranularity(Granularity.GRANULARITY_FINE)
            setWaitForAccurateLocation(true)
        }.build()
    }

    fun getLocationCallback(onUpdated: (Location?) -> Unit) {
        checkLocationAvailability()
        checkLocationPermissions()
        checkBatterySaverMode()
        checkAirplaneModeOn()

        createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    onUpdated.invoke(location)
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        kotlin.runCatching {
            locationCallback?.let { client.removeLocationUpdates(it) }

            if (locationRequest == null) createLocationRequest()

            locationCallback?.let { client.requestLocationUpdates(locationRequest!!, it, null) }
        }.onFailure {
            recordError("startLocationUpdates", it)
        }
    }

    fun stopLocationUpdates() {
        kotlin.runCatching {
            locationCallback?.let { client.removeLocationUpdates(it) }

            locationCallback = null
            locationRequest = null
        }.onFailure {
            recordError("stopLocationUpdates", it)
        }
    }

    fun checkLocationAvailability(): String {
        val message = "GPS tidak aktif"

        return runCatching {
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER)

            return if (!isGpsEnabled && !isNetworkEnabled) {
                recordError("checkLocationAvailability", Exception(message))
                message
            } else ""
        }.getOrElse {
            recordError("checkLocationAvailability", it)
            message
        }
    }

    fun checkLocationPermissions(): String {
        val message = "Izin lokasi tidak diberikan"

        return runCatching {
            return if (checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                recordError("checkLocationPermissions", Exception(message))
                message
            } else ""
        }.getOrElse {
            recordError("checkLocationPermissions", it)
            message
        }
    }

    fun checkBatterySaverMode(): String {
        val message = "Mode hemat daya aktif"

        return runCatching {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isBatterySaverOn = powerManager.isPowerSaveMode
            return if (isBatterySaverOn) {
                recordError("checkBatterySaverMode", Exception(message))
                message
            } else ""
        }.getOrElse {
            recordError("checkBatterySaverMode", it)
            message
        }
    }

    fun checkAirplaneModeOn(): String {
        val message = "Mode pesawat aktif"

        return runCatching {
            val isAirplaneModeOn = Settings.Global.getInt(context.contentResolver, AIRPLANE_MODE_ON, 0) != 0
            return if (isAirplaneModeOn) {
                recordError("checkAirplaneModeOn", Exception(message))
                message
            } else ""
        }.getOrElse {
            recordError("checkAirplaneModeOn", it)
            message
        }
    }

    private fun recordError(label: String, error: Throwable) {
        println("cek error $label: ${error.message}")
    }

    companion object {
        fun checkAllLocationPermissionGranted(context: Context, includeBackgroundLocation: Boolean = false): Boolean {
            val permissions = mutableListOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            if (includeBackgroundLocation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissions.add(ACCESS_BACKGROUND_LOCATION)
            }

            return permissions.all { permissionName ->
                checkSelfPermission(context, permissionName) == PERMISSION_GRANTED
            }
        }

        fun calculateDistanceInMeters(startLocation: LatLng, endLocation: LatLng): Float {
            val results = FloatArray(1) // Array to store the distance result
            Location.distanceBetween(
                startLocation.latitude,
                startLocation.longitude,
                endLocation.latitude,
                endLocation.longitude,
                results
            )
            return results[0] // The distance in meters is stored at index 0
        }
    }
}
