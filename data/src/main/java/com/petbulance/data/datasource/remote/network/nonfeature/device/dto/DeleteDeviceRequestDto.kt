package com.petbulance.data.datasource.remote.network.nonfeature.device.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteDeviceRequestDto(
    val fcmToken: String
)
