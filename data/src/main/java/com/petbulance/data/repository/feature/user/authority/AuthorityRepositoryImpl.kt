package com.petbulance.data.repository.feature.user.authority

import com.petbulance.data.datasource.remote.network.feature.user.authority.AuthorityApi
import com.petbulance.data.datasource.remote.network.feature.user.authority.dto.AuthorityResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.domain.model.feature.user.user.AuthoritySettings
import com.petbulance.domain.repository.feature.user.AuthorityRepository
import javax.inject.Inject

class AuthorityRepositoryImpl @Inject constructor(
    private val api: AuthorityApi
) : AuthorityRepository {

    override suspend fun getAuthority(): Result<AuthoritySettings> {
        return safeApiCall<AuthorityResDto>(path = "/users/authority") {
            api.getAuthority()
        }.map {
            AuthoritySettings(
                locationService = it.locationService,
                marketing = it.marketing,
                camera = it.camera
            )
        }
    }

    override suspend fun toggleAuthority(type: String): Result<Unit> {
        return safeApiCall<Map<String, String>>(path = "/users/authority/$type") {
            api.toggleAuthority(type)
        }.map { }
    }
}
