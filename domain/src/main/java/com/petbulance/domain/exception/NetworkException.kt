package com.petbulance.domain.exception

import java.io.IOException

/**
 * 400, 500번대 오류 handling을 위한 예외
 */

/**
 * 잘못된 아이디/비밀번호 등 인증 실패 (401)
 */
class InvalidCredentialsException(message: String) : ClientException(401, message)

/**
 * 잘못된 요청 (400)
 */
class BadRequestException(message: String) : ClientException(400, message)

/**
 * 리소스를 찾을 수 없음 (404)
 */
class NotFoundException(message: String) : ClientException(404, message)

/**
 * 내부 서버 오류 (500)
 */
class InternalServerErrorException(message: String) : ServerException(500, message)

/**
 * 알 수 없는 네트워크 오류
 */
class UnknownNetworkException(message: String) : IOException(message)