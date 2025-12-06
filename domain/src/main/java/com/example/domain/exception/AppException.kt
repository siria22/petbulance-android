package com.example.domain.exception

import java.io.IOException

/**
 * 앱 로직 처리 중 발생하는 예외 (네트워크, 데이터베이스 오류 제외)
 */
open class LogicException(message: String? = null) : RuntimeException(message)

/**
 * 클라이언트 측 오류를 나타내는 예외 (4xx 에러 등)
 */
open class ClientException(val code: Int, message: String) : IOException("클라이언트 오류: $message")

/**
 * 서버 측 오류를 나타내는 예외 (5xx 에러 등)
 */
open class ServerException(val code: Int, message: String) : IOException("서버 오류: $message")

/**
 * 200번대 응답이지만 데이터 파싱에 실패함 (값 없음 or 파싱 실패)
 */
open class EmptyDataException(message: String) : Exception("데이터 파싱 실패: $message")