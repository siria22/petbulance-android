package com.example.data.di.security

import android.util.Base64
import kotlin.text.split

fun EncryptedData.toBase64String(): String {
    val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
    val encryptedBytesBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    return "$ivBase64:$encryptedBytesBase64"
}

fun String.toEncryptedData(): EncryptedData? {
    return try {
        val parts = this.split(":")
        if (parts.size != 2) return null

        val iv = Base64.decode(parts[0], Base64.DEFAULT)
        val encryptedBytes = Base64.decode(parts[1], Base64.DEFAULT)
        EncryptedData(encryptedBytes, iv)
    } catch (e: Exception) {
        null
    }
}