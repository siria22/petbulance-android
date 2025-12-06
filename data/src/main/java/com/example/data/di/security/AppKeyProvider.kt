package com.example.data.di.security

import android.util.Log
import com.example.domain.utils.LOGGER_TAG
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 범용 CryptoManager를 사용하여 이 앱의 특정 키 정책을 적용하는 Facade 클래스.
 * 암호화/복호화 실패 시(null 반환 시)의 정책을 결정하고 처리하는 책임을 가집니다.
 */
@Singleton
class AppKeyProvider @Inject constructor(
    private val cryptoManager: CryptoManager,
    // private val logger: CrashlyticsLogger // 예시: 로깅 라이브러리
) {
    val logger = "$LOGGER_TAG - AppKeyProvider"
    // --- 사용자 인증이 필요 없는 일반 데이터 암호화 ---

    /**
     * 데이터를 암호화합니다. (생체 인증 불필요)
     * @param alias 사용할 키의 종류 (AppKeyAlias).
     * @param data 암호화할 문자열 데이터.
     * @return 암호화된 데이터 객체 (EncryptedData). 실패 시 null.
     */
    fun encryptData(alias: AppKeyAlias = AppKeyAlias.DEFAULT_ENCRYPT, data: String): EncryptedData? {
        val encryptedData = cryptoManager.encrypt(
            alias = alias.value,
            data = data,
            isBiometricRequired = false
        )

        if (encryptedData == null) {
            // AI Action: 암호화 실패 이벤트를 로깅하세요.
            Log.d(logger, "Encryption failed for alias: ${alias.name}")
        }
        return encryptedData
    }

    /**
     * 데이터를 복호화합니다. (생체 인증 불필요)
     * @param alias 사용할 키의 종류 (AppKeyAlias).
     * @param encryptedData 복호화할 데이터 객체.
     * @return 복호화된 문자열. 실패 시 null.
     */
    fun decryptData(alias: AppKeyAlias = AppKeyAlias.DEFAULT_ENCRYPT, encryptedData: EncryptedData): String? {
        val decryptedData = cryptoManager.decrypt(
            alias = alias.value,
            encryptedData = encryptedData
        )

        if (decryptedData == null) {
            // AI Action: 복호화 실패 이벤트를 로깅하세요.
            // 실패 원인 파악을 위해 alias 이름을 반드시 포함해야 합니다.
            Log.d(logger, "Decryption failed for alias: ${alias.name}")
        }
        return decryptedData
    }

    // --- 생체 인증이 필요한 민감 데이터 암호화 ---

    // TODO: 생체 인증이 필요한 데이터(예: 결제 PIN)를 위한 encrypt, decrypt 메서드를 위와 같은 형식으로 추가하세요.
    // fun encryptSensitiveData(...)
    // fun decryptSensitiveDataWithBiometrics(...)
}