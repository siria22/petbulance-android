package com.petbulance.data.repository.feature.hospital.hospital

import com.petbulance.data.datasource.remote.network.common.CursorPagingResponse
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.HospitalApi
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalCardResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalDetailResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalSearchReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalsResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.hospital.toDomain
import com.petbulance.domain.model.common.PagingResult
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val api: HospitalApi
) : HospitalRepository {

    override suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
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
        return safeApiCall<CursorPagingResponse<HospitalsResDto>>(
            path = "/hospitals"
        ) {
            api.searchHospitals(
                HospitalSearchReqDto(
                    q = q,
                    region = region,
                    lat = lat,
                    lng = lng,
                    bounds = bounds,
                    animal = animal,
                    openNow = openNow,
                    sortBy = sortBy,
                    size = size,
                    cursorId = cursorId,
                    cursorDistance = cursorDistance,
                    cursorRating = cursorRating,
                    cursorReviewCount = cursorReviewCount
                )
            )
        }.map { pagingResponse ->
            PagingResult(
                content = pagingResponse.list.map { it.toDomain() },
                hasNext = pagingResponse.hasNext,
                cursorId = pagingResponse.cursorId,
                cursorDistance = pagingResponse.cursorDistance,
                cursorRating = pagingResponse.cursorRating,
                cursorReviewCount = pagingResponse.cursorReviewCount
            )
        }
    }

    override suspend fun getHospitalDetail(hospitalId: Long): Result<HospitalDetail> {
        return safeApiCall<HospitalDetailResDto>(path = "/hospitals/$hospitalId") {
            api.searchHospitalDetail(hospitalId)
        }.map {
            it.toDomain()
        }
    }

    override suspend fun getHospitalCard(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalCard> {
        return safeApiCall<HospitalCardResDto>(path = "/hospitals/card/$hospitalId") {
            api.searchHospitalCard(hospitalId, userLat, userLng)
        }.map { it.toDomain() }
    }

}