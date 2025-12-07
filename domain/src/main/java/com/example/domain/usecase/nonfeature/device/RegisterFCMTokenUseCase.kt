package com.example.domain.usecase.nonfeature.device

import com.example.domain.repository.nonfeature.device.DeviceRepository
import javax.inject.Inject

class RegisterFCMTokenUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend operator fun invoke() {
        return repository.addDevice().getOrThrow()
    }
}