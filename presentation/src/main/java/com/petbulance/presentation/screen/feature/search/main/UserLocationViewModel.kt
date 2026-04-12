package com.petbulance.presentation.screen.feature.search.main

import android.location.Location
import com.petbulance.domain.repository.nonfeature.device.LocationProvider
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class UserLocationViewModel @Inject constructor(
    private val locationProvider: LocationProvider
) : BaseViewModel() {

    private val _state = MutableStateFlow<UserLocationState>(UserLocationState.Init)
    val locationState: StateFlow<UserLocationState> = _state

    private val _eventFlow = MutableSharedFlow<SearchEvent>()
    val eventFlow: SharedFlow<SearchEvent> = _eventFlow

    private var isInitialLocationSent = false

    fun onIntent(intent: UserLocationIntent) {
        when (intent) {
            is UserLocationIntent.RequestLocation -> {
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

    private fun startLocationUpdates(forceMove: Boolean = false) {
        launch {
            var shouldMoveCamera = forceMove
            _state.value = UserLocationState.Finding

            locationProvider.getLocationUpdates()
                .catch { ex ->
                    _state.value = UserLocationState.PermissionRequired
                    _eventFlow.emit(
                        SearchEvent.UserLocation.CheckPermission.Error(
                            exceptionMessage = ex.message
                        )
                    )
                }
                .collect { deviceLocation ->
                    val location = Location("fused").apply {
                        latitude = deviceLocation.latitude
                        longitude = deviceLocation.longitude
                    }
                    _state.value = UserLocationState.Success(location)
                    if (shouldMoveCamera || !isInitialLocationSent) {
                        _eventFlow.emit(SearchEvent.UserLocation.MoveCamera(location))
                        isInitialLocationSent = true
                        shouldMoveCamera = false
                    }
                }
        }
    }
}