package com.petbulance.domain.model.feature.hospital.hospital

data class HospitalMarker(
    val hospitalId: Long,
    val longitude: Double,
    val latitude: Double,
    val isOpened: Boolean,
    val isSelected: Boolean
)