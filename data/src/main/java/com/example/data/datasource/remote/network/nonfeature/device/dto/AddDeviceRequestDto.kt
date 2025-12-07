package com.example.data.datasource.remote.network.nonfeature.device.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddDeviceRequestDto(
    val fcmToken: String,
    val deviceOs: String
)
