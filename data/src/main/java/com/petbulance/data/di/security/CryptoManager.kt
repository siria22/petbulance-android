package com.petbulance.data.di.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import com.petbulance.domain.utils.LOGGER_TAG
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/**
 * Android Keystore를 사용하여 데이터를 안전하게 암호화하고 복호화하는 범용 클래스.
 * 이 클래스는 특정 프로젝트의 정책에 의존하지 않으며, 재사용이 가능합니다.
 *
 * @see AppKeyProvider 와 같은 Facade 클래스를 통해 사용하는 것을 권장합니다.
 */
class CryptoManager {

    private val logger = "$LOGGER_TAG - CryptoManager"
    // Android Keystore 인스턴스를 로드합니다.
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    /**
     * 데이터를 암호화합니다.
     * 지정된 alias로 키가 존재하지 않으면, isBiometricRequired 정책에 따라 새로 생성합니다.
     *
     * @param alias 암호화 키를 식별하는 고유한 문자열.
     * @param data 암호화할 평문 문자열.
     * @param isBiometricRequired 이 키 사용 시 생체 인증이 필요한지 여부. (키 생성 시에만 사용)
     * @return 암호화된 데이터(ByteArray)와 IV를 포함하는 EncryptedData 객체.
     */
    fun encrypt(alias: String, data: String, isBiometricRequired: Boolean): EncryptedData? {
        return try {
            val cipher = getEncryptCipher(alias, isBiometricRequired)
            val encryptedBytes = cipher.doFinal(data.toByteArray(Charset.defaultCharset()))
            EncryptedData(encryptedBytes, cipher.iv)
        } catch (e: Exception) {
            // TODO: 암호화 실패 시 예외 처리 로직을 구현해야 합니다.
            //  - 실패 로그를 기록합니다.
            //  - 사용자에게 알림을 보내거나, 특정 기본값으로 대체할 수 있습니다.
            //  - 실패 시 null을 리턴합니다.
            Log.e(logger, "Encryption failed for alias: $alias", e)
            null
        }
    }

    /**
     * 생체 인증 없이 데이터를 복호화합니다.
     * isBiometricRequired가 false로 설정된 키로 암호화된 데이터에만 사용해야 합니다.
     *
     * @param alias 복호화에 사용할 키의 alias.
     * @param encryptedData 암호화된 데이터와 IV를 포함하는 객체.
     * @return 복호화된 평문 문자열.
     */
    fun decrypt(alias: String, encryptedData: EncryptedData): String? {
        return try {
            val cipher = getDecryptCipher(alias, encryptedData.iv)
            val decryptedBytes = cipher.doFinal(encryptedData.encryptedBytes)
            String(decryptedBytes, Charset.defaultCharset())
        } catch (e: Exception) {
            // TODO: 복호화 실패 시 예외 처리 로직을 구현해야 합니다.
            //  - 키가 무효화되었거나(예: 지문 변경), 데이터가 손상되었을 수 있습니다.
            //  - 사용자에게 재로그인을 유도하거나 데이터를 초기화해야 할 수 있습니다.
            Log.e(logger, "Decryption failed for alias: $alias", e)
            null // 실패 시 null 반환
        }
    }

    /**
     * BiometricPrompt 인증 성공 후 반환된 Cipher 객체로 데이터를 복호화합니다.
     *
     * @param cipher BiometricPrompt.AuthenticationResult에서 받은, 인증이 완료된 Cipher 객체.
     * @param encryptedBytes 복호화할 데이터.
     * @return 복호화된 평문 문자열.
     */
    fun decryptWithCipher(cipher: Cipher, encryptedBytes: ByteArray): String? {
        return try {
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            String(decryptedBytes, Charset.defaultCharset())
        } catch (e: Exception) {
            // TODO: 최종 복호화 단계에서의 실패 처리 로직을 구현
            Log.e(logger, "Decryption with biometric cipher failed", e)
            null
        }
    }

    /**
     * BiometricPrompt에 전달할, 생체 인증용 복호화 Cipher를 생성합니다.
     *
     * @param alias 사용할 키의 alias.
     * @param iv 암호화 시 사용된 IV.
     * @return 초기화된 복호화 Cipher 객체.
     */
    fun getBiometricDecryptCipher(alias: String, iv: ByteArray): Cipher {
        val secretKey = getExistingKeyOrCreate(alias, isBiometricRequired = true) // 생체인증용 키 가져오기
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return cipher
    }

    private fun getEncryptCipher(alias: String, isBiometricRequired: Boolean): Cipher {
        val secretKey = getExistingKeyOrCreate(alias, isBiometricRequired)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    private fun getDecryptCipher(alias: String, iv: ByteArray): Cipher {
        val secretKey = getExistingKeyOrCreate(alias, isBiometricRequired = false) // 일반 키 가져오기
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return cipher
    }

    private fun getExistingKeyOrCreate(alias: String, isBiometricRequired: Boolean): SecretKey {
        val existingKey = keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry
        if (existingKey != null) {
            return existingKey.secretKey
        }
        return createKey(alias, isBiometricRequired)
    }

    private fun createKey(alias: String, isBiometricRequired: Boolean): SecretKey {
        val keyBuilder = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(isBiometricRequired)
            .setRandomizedEncryptionRequired(true)

        // 생체 인증 키의 경우, 새 지문 등록 시 키를 무효화하여 보안 강화
        if (isBiometricRequired) {
            keyBuilder.setInvalidatedByBiometricEnrollment(true)
        }

        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(keyBuilder.build())
        }.generateKey()
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}

data class EncryptedData(
    val encryptedBytes: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedData

        if (!encryptedBytes.contentEquals(other.encryptedBytes)) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encryptedBytes.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}