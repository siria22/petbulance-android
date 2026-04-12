package com.petbulance.domain.repository.nonfeature.app

/**
 * URI로부터 파일의 바이트 데이터와 MIME 타입을 읽는 인터페이스.
 * ViewModel에서 android.content.Context 의존성을 제거하기 위해 사용.
 */
interface ContentFileReader {
    suspend fun readBytes(uriString: String): ContentFileData?
}

data class ContentFileData(
    val bytes: ByteArray,
    val mimeType: String
)
