package com.example.domain.model.feature.hospital.recent

data class ViewedHospitalList(
    val items: List<ViewedHospital>,
    val totalCount: Long
)

data class ViewedHospital(
    val hospitalId: Long,
    val hospitalName: String,
    val viewedAt: String
)