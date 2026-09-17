package com.petbulance.data.repository.feature.hospital.hospital

import com.petbulance.data.repository.MockFixtures
import com.petbulance.data.repository.MockHospital
import com.petbulance.domain.model.common.PagingResult
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class MockHospitalRepository @Inject constructor() : HospitalRepository {

    override suspend fun searchHospitals(
        q: String?,
        region: String?,
        bounds: String?,
        animal: String?,
        openNow: Boolean?,
        sortBy: String?,
        size: Int,
        cursorId: Long?,
        cursorDistance: Double?,
        cursorRating: Double?,
        cursorReviewCount: Long?
    ): Result<PagingResult<Hospital>> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val now = LocalDateTime.now()
        val animals = animal?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }.orEmpty()
        val area = bounds?.toBounds()

        val filtered = MockFixtures.hospitals.filter { hospital ->
            hospital.matchesQuery(q) &&
                    (animals.isEmpty() || hospital.animals.any { it.name in animals }) &&
                    (openNow != true || hospital.isOpenAt(now)) &&
                    (area == null || area.contains(hospital))
        }
        val startIndex = cursorId?.let { id -> filtered.indexOfFirst { it.id == id } + 1 } ?: 0
        val page = filtered.drop(startIndex).take(size)

        return Result.success(
            PagingResult(
                content = page.map { it.toHospital(now) },
                hasNext = startIndex + page.size < filtered.size,
                cursorId = page.lastOrNull()?.id
            )
        )
    }

    override suspend fun getHospitalDetail(hospitalId: Long): Result<HospitalDetail> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val hospital = MockFixtures.findHospital(hospitalId)
            ?: return Result.failure(NoSuchElementException("Hospital not found: $hospitalId"))
        val now = LocalDateTime.now()

        return Result.success(
            HospitalDetail(
                hospitalId = hospital.id,
                name = hospital.name,
                address = hospital.address,
                lat = hospital.lat,
                lng = hospital.lng,
                phone = hospital.phone,
                acceptedAnimals = hospital.species.map { it.korean },
                openHours = hospital.openHours,
                notes = hospital.notes,
                openNow = hospital.isOpenAt(now),
                description = hospital.description,
                rating = MockFixtures.ratingOf(hospital.id),
                reviewCount = MockFixtures.reviewsOf(hospital.id).size,
                thumbnailUrl = null,
                tags = hospital.tags
            )
        )
    }

    override suspend fun getHospitalCard(hospitalId: Long): Result<HospitalCard> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val hospital = MockFixtures.findHospital(hospitalId)
            ?: return Result.failure(NoSuchElementException("Hospital not found: $hospitalId"))
        val now = LocalDateTime.now()

        return Result.success(
            HospitalCard(
                hospitalId = hospital.id,
                name = hospital.name,
                lat = hospital.lat,
                lng = hospital.lng,
                distanceMeters = 0.0,
                phone = hospital.phone,
                types = hospital.species.map { it.name },
                isOpenNow = hospital.isOpenAt(now),
                nextOpenHours = hospital.openHoursLabel(now),
                thumbnailUrl = "",
                rating = MockFixtures.ratingOf(hospital.id),
                reviewCount = MockFixtures.reviewsOf(hospital.id).size.toLong(),
                image = null
            )
        )
    }

    private fun MockHospital.matchesQuery(query: String?): Boolean {
        val keyword = query?.trim().orEmpty()
        if (keyword.isEmpty()) return true
        return name.contains(keyword) ||
                address.contains(keyword) ||
                tags.any { it.value.contains(keyword) } ||
                species.any { it.korean.contains(keyword) } ||
                animals.any { it.korean.contains(keyword) }
    }

    private fun MockHospital.toHospital(now: LocalDateTime) = Hospital(
        hospitalId = id,
        name = name,
        lat = lat,
        lng = lng,
        distanceMeters = null,
        phone = phone,
        types = species.map { it.name },
        isOpenNow = isOpenAt(now),
        openHours = openHoursLabel(now),
        thumbnailUrl = null,
        rating = MockFixtures.ratingOf(id),
        reviewCount = MockFixtures.reviewsOf(id).size,
        image = null,
        tags = tags
    )

    private data class Bounds(val minLat: Double, val minLng: Double, val maxLat: Double, val maxLng: Double) {
        fun contains(hospital: MockHospital): Boolean =
            hospital.lat in minLat..maxLat && hospital.lng in minLng..maxLng
    }

    /** `SearchHospitalsUseCase`가 만드는 "minLat,minLng,maxLat,maxLng" 형식을 읽는다. */
    private fun String.toBounds(): Bounds? {
        val values = split(",").mapNotNull { it.trim().toDoubleOrNull() }
        if (values.size != BOUNDS_VALUE_COUNT) return null
        return Bounds(values[0], values[1], values[2], values[3])
    }

    companion object {
        private const val BOUNDS_VALUE_COUNT = 4
    }
}
