package com.petbulance.presentation.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun buildHighlightedText(
    fullText: String,
    searchKeyword: String
): AnnotatedString {
    if (searchKeyword.isBlank()) {
        return AnnotatedString(fullText)
    }

    return buildAnnotatedString {
        var currentIndex = 0
        val lowerFullText = fullText.lowercase()
        val lowerKeyword = searchKeyword.lowercase()

        while (currentIndex < fullText.length) {
            val matchIndex = lowerFullText.indexOf(lowerKeyword, currentIndex)
            
            if (matchIndex == -1) {
                append(fullText.substring(currentIndex))
                break
            }

            if (matchIndex > currentIndex) {
                append(fullText.substring(currentIndex, matchIndex))
            }

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(fullText.substring(matchIndex, matchIndex + searchKeyword.length))
            }

            currentIndex = matchIndex + searchKeyword.length
        }
    }
}
