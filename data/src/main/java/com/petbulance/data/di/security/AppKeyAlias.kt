package com.petbulance.data.di.security

/**
 * 앱 내의 암호화/복호화 시에 사용할 Key Alias와 관련된 Enum Class.
 * 별도의 policy 및 category가 필요할 경우 추가하여 사용.
 *
 * @property value The string value of the alias.
 */
enum class AppKeyAlias(val value: String) {
    DEFAULT_ENCRYPT("default_encrypt"),
    ACCESS_TOKEN_KEY("access_token_key"),
    REFRESH_TOKEN_KEY("refresh_token_key")
}
