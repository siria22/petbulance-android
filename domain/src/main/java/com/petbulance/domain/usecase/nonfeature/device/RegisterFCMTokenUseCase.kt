package com.petbulance.domain.usecase.nonfeature.device

import com.petbulance.domain.repository.nonfeature.device.DeviceRepository
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke() {
        return repository.addDevice().getOrThrow()
    }
}