package com.example.data.mapper

import com.example.data.datasource.local.database.entity.ExampleEntity
import com.example.domain.model.ExampleModel

fun ExampleModel.toEntity(): ExampleEntity {
    return ExampleEntity(id = id, name = name)
}

fun ExampleEntity.toDomain(): ExampleModel {
    return ExampleModel(id = id, name = name)
}