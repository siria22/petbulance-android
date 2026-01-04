package com.example.domain.model.type

enum class LoginProviderType(val korean: String) {
    GOOGLE("구글"),
    NAVER("네이버"),
    KAKAO("카카오");

    companion object {
        fun fromString(value: String?): LoginProviderType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: GOOGLE
        }

        fun toKorean(value: LoginProviderType): String {
            return entries.find { it.name.equals(value.name, ignoreCase = true) }?.name
                ?: "(알 수 없음)"
        }
    }
}