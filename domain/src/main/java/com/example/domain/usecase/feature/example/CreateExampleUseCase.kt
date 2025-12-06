package com.example.domain.usecase.feature.example

import com.example.domain.model.ExampleModel
import com.example.domain.repository.feature.ExampleRepository
import javax.inject.Inject

class CreateExampleUseCase @Inject constructor(
    private val exampleRepository: ExampleRepository
) {
    suspend operator fun invoke(newExampleModel: ExampleModel): Result<Unit> {
        return exampleRepository.createExampleEntity(newExampleModel)
    }
}
