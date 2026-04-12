package com.petbulance.domain.model.feature.support.notice

data class NoticeButton(
    val buttonId: Long,
    val text: String,
    val link: String,
    val target: NoticeButtonTarget
)

enum class NoticeButtonTarget {
    INNER, EXTERNAL;

    companion object {
        fun fromString(value: String?): NoticeButtonTarget =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: EXTERNAL
    }
}
