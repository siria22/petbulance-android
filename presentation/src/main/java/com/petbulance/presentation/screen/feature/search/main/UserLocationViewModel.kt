package com.petbulance.presentation.screen.feature.search.main

import android.annotation.SuppressLint
import android.os.Looper
import com.petbulance.presentation.utils.BaseViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : BaseViewModel() {

    private val _state = MutableStateFlow<UserLocationState>(UserLocationState.Init)
    val locationState: StateFlow<UserLocationState> = _state

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private var isInitialLocationSent = false

    fun onIntent(intent: UserLocationIntent) {
        when (intent) {
            is UserLocationIntent.RequestLocation -> {
                // UI에서 권한 체크 후 없으면 PermissionRequired 상태로, 있으면 수집 시작
                // 여기서는 일단 수집 시도 (권한 없으면 에러 발생 -> catch)
                startLocationUpdates(forceMove = true)
            }

            is UserLocationIntent.PermissionResult -> {
                if (intent.isGranted) {
                    startLocationUpdates()
                } else {
                    _state.value = UserLocationState.PermissionRequired
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(forceMove: Boolean = false) {
        launch {
            _state.value = UserLocationState.Finding

            getLocationFlow()
                .catch { ex ->
                    _state.value = UserLocationState.PermissionRequired
                    _eventFlow.emit(
                        SearchEvent.UserLocation.CheckPermission.Error(
                            exceptionMessage = ex.message
                        )
                    )
                }
                .collect { location ->
                    _state.value = UserLocationState.Success(location)
                    if (forceMove || !isInitialLocationSent) {
                        _eventFlow.emit(SearchEvent.UserLocation.MoveCamera(location))
                        isInitialLocationSent = true
                    }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationFlow() = callbackFlow {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(5000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }
        try {
            fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
                .addOnFailureListener { e ->
                    close(e)
                }
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }
}