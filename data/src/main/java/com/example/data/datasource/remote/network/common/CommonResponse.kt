package com.example.data.datasource.remote.network.common

import kotlinx.serialization.Serializable

/**
 * CommonResponse
 *
 * BaseResponse와 ErrorResponse를 정의한 파일.
 * 프로젝트 요구사항에 따라 공통 응답구조가 달라질 수 있으므로 반드시 명세 참고할 것.
 */

/**
 * T : Response DTO
 */
@Serializable
data class BaseResponse<T>(
    val status: Int,
    val success: Boolean,
    val data: T? = null,
    val message: String? = null
)

/**
 * T : Response DTO, null if no extra data from server
 */
@Serializable
data class ErrorResponse(
    val className: String,
    val message: String
)
