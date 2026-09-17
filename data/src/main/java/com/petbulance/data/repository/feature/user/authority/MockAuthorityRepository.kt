package com.petbulance.data.repository.feature.user.authority

import com.petbulance.domain.model.feature.user.user.AuthoritySettings
import com.petbulance.domain.repository.feature.user.AuthorityRepository
import javax.inject.Inject

class MockAuthorityRepository @Inject constructor() : AuthorityRepository {

    private var settings = AuthoritySettings(locationService = true, marketing = false, camera = true)

    override suspend fun getAuthority(): Result<AuthoritySettings> = Result.success(settings)

    override suspend fun toggleAuthority(type: String): Result<Unit> {
        settings = when (type) {
            TYPE_LOCATION -> settings.copy(locationService = !settings.locationService)
            TYPE_MARKETING -> settings.copy(marketing = !settings.marketing)
            TYPE_CAMERA -> settings.copy(camera = !settings.camera)
            else -> return Result.failure(IllegalArgumentException("Unknown authority type: $type"))
        }
        return Result.success(Unit)
    }

    companion object {
        private const val TYPE_LOCATION = "locationService"
        private const val TYPE_MARKETING = "marketing"
        private const val TYPE_CAMERA = "camera"
    }
}
