package com.petbulance.data.repository.nonfeature.device

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.petbulance.domain.repository.nonfeature.device.DeviceLocation
import com.petbulance.domain.repository.nonfeature.device.LocationProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FusedLocationProviderImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<DeviceLocation> = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_MS)
            .setMinUpdateIntervalMillis(MIN_UPDATE_INTERVAL_MS)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(DeviceLocation(location.latitude, location.longitude))
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnFailureListener { e -> close(e) }
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    companion object {
        private const val UPDATE_INTERVAL_MS = 10000L
        private const val MIN_UPDATE_INTERVAL_MS = 5000L
    }
}
