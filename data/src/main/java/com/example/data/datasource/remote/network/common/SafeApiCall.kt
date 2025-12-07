package com.example.data.datasource.remote.network.common

import android.util.Log
import com.example.domain.exception.BadRequestException
import com.example.domain.exception.ClientException
import com.example.domain.exception.EmptyDataException
import com.example.domain.exception.InternalServerErrorException
import com.example.domain.exception.InvalidCredentialsException
import com.example.domain.exception.NotFoundException
import com.example.domain.exception.ServerException
import com.example.domain.exception.UnknownNetworkException
import com.example.domain.utils.LOGGER_TAG
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json


/**
 * SafeApiCall
 *
 * CommonResponse에 정의된 BaseResponse<T>, ErrorResponse<T>를 이용하여,
 * 결과를 Result<T> 형식으로 반환하는 함수.
 *
 * @param apiCall 실제 API 호출을 수행하는 람다
 */
suspend inline fun <reified T> safeApiCall(
    path: String,
    crossinline apiCall: suspend () -> HttpResponse
): Result<T> {
    val logger = "$LOGGER_TAG - SafeApiCalls"

    try {
        val response = apiCall()
        val responseString = response.body<String>()
        val json = Json { ignoreUnknownKeys = true }

        val statusCode = response.status.value
        Log.d("$logger (responseString)", "[$statusCode] : $responseString")

        when (statusCode) {
            in 200..299 -> {
                val responseBody = json.decodeFromString<BaseResponse<T>>(responseString)
                return responseBody.data?.let { Result.success(it) }
                    ?: Result.failure(EmptyDataException("Response data is null"))
            }

            in 400..599 -> {
                val errorBody = json.decodeFromString<BaseResponse<ErrorResponse>>(responseString)
                return Result.failure(
                    mapToDomainException(errorBody.status, path, errorBody.data!!.className, errorBody.data.message)
                )
            }

            else -> {
                return Result.failure(
                    UnknownNetworkException("Unknown error with code $statusCode.")
                )
            }
        }
    } catch (e: Exception) {
        val errorMessage = "Exception in SafeApiCall for path: $path"
        Log.e("$logger (Error)", errorMessage, e)
        return Result.failure(Exception(errorMessage, e))
    }
}

fun mapToDomainException(code: Int, requestPath: String, className: String, message: String): Exception {
    val errorMessage = "Request failed for path: $requestPath\n" +
            "Error class name = $className\n" +
            "Reason: $message"
    return when (code) {
        400 -> BadRequestException(errorMessage)
        401 -> InvalidCredentialsException(errorMessage)
        404 -> NotFoundException(errorMessage)
        500 -> InternalServerErrorException(errorMessage)
        else -> if (code in 400..499) {
            ClientException(code, errorMessage)
        } else if (code in 500..599) {
            ServerException(code, errorMessage)
        } else {
            UnknownNetworkException("Unknown error with code $code: $errorMessage")
        }
    }
}