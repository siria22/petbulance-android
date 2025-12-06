package com.example.domain.model

data class ExampleModel(
    val id: Long,
    val name: String
) {
    companion object {
        fun empty() = ExampleModel(
            id = 0,
            name = ""
        )
    }
}