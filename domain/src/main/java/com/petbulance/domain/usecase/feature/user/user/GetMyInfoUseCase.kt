package com.petbulance.domain.usecase.feature.user.user

import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class GetMyInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserInfo> {
        return userRepository.getMyInfo()
    }
}