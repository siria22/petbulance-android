package com.example.domain.repository.feature

import com.example.domain.model.ExampleModel

interface ExampleRepository {
    suspend fun createExampleEntity(exampleModel: ExampleModel): Result<Unit>

}