package com.petbulance.data.repository.feature.hospital.history

import com.petbulance.data.repository.MockFixtures
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class MockSearchRepository @Inject constructor() : SearchRepository {

    private val keywords = MutableStateFlow(
        INITIAL_KEYWORDS.mapIndexed { index, keyword ->
            RecentSearchKeyword(id = index + 1L, keyword = keyword, date = LocalDate.now().minusDays(index.toLong()).toString())
        }
    )

    private val viewedHospitals = MutableStateFlow(
        INITIAL_VIEWED_HOSPITAL_IDS.mapIndexedNotNull { index, id ->
            MockFixtures.findHospital(id)?.let { hospital ->
                ViewedHospital(
                    hospitalId = hospital.id,
                    hospitalName = hospital.name,
                    viewedAt = LocalDateTime.now().minusHours(index.toLong()).withNano(0).toString()
                )
            }
        }
    )

    override fun getSearchHistoryStream(): Flow<List<RecentSearchKeyword>> = keywords

    override suspend fun syncSearchHistory() = Unit

    override suspend fun addSearchKeyword(keyword: String) {
        keywords.update { current ->
            val nextId = (current.maxOfOrNull { it.id } ?: 0L) + 1
            listOf(RecentSearchKeyword(nextId, keyword, LocalDate.now().toString())) +
                    current.filterNot { it.keyword == keyword }
        }
    }

    override suspend fun deleteSearchKeyword(keyword: String) {
        keywords.update { current -> current.filterNot { it.keyword == keyword } }
    }

    override fun getViewedHospitalsStream(): Flow<List<ViewedHospital>> = viewedHospitals

    override suspend fun syncViewedHospitals() = Unit

    override suspend fun addViewedHospital(hospitalId: Long, hospitalName: String) {
        viewedHospitals.update { current ->
            listOf(ViewedHospital(hospitalId, hospitalName, LocalDateTime.now().withNano(0).toString())) +
                    current.filterNot { it.hospitalId == hospitalId }
        }
    }

    override suspend fun deleteViewedHospital(hospitalId: Long) {
        viewedHospitals.update { current -> current.filterNot { it.hospitalId == hospitalId } }
    }

    companion object {
        private val INITIAL_KEYWORDS = listOf("토끼 부정교합", "24시 특수동물병원", "앵무새 깃털")
        private val INITIAL_VIEWED_HOSPITAL_IDS = listOf(5L, 4L, 2L)
    }
}
