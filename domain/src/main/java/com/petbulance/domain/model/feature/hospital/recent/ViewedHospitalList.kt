package com.petbulance.domain.model.feature.hospital.recent

data class ViewedHospitalList(
    val items: List<ViewedHospital>,
    val totalCount: Long
) {
    companion object {
        fun stub() = ViewedHospitalList(
            items = listOf(
                ViewedHospital.stub(),
                ViewedHospital.stub().copy(hospitalId = 1),
                ViewedHospital.stub().copy(hospitalId = 2)
            ),
            totalCount = 0
        )

        val empty = ViewedHospitalList(items = emptyList(), totalCount = 0)
    }
}

data class ViewedHospital(
    val hospitalId: Long,
    val hospitalName: String,
    val viewedAt: String
) : ContentAsString {
    companion object {
        fun stub() = ViewedHospital(
            hospitalId = 0,
            hospitalName = "Dummy Hospital",
            viewedAt = "2025-12-01"
        )
    }

    override fun getContentAsString(): String = hospitalName
}