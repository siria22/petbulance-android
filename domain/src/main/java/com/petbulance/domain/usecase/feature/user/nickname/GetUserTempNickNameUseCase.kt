package com.petbulance.domain.usecase.feature.user.nickname

import com.petbulance.domain.repository.feature.user.UserRepository
import javax.inject.Inject

class GetUserTempNickNameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<String> {
        return repository.getMyInfo().map { it.nickname }
    }
}