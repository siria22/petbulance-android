package com.example.data.repository

import com.example.data.mapper.toEntity
import com.example.data.datasource.local.database.dao.ExampleDao
import com.example.domain.model.ExampleModel
import com.example.domain.repository.feature.ExampleRepository
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val exampleDao: ExampleDao
) : ExampleRepository {

    override suspend fun createExampleEntity(exampleModel: ExampleModel) =
        runCatching {
            exampleDao.createExampleEntity(exampleModel.toEntity())
        }.onFailure { ex ->
            throw Exception(ex)
        }
}