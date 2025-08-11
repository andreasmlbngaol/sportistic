package com.jawapbo.sportistic.core.utils

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object Securer {
    private const val AES_KEY_SIZE = 32 // 256 bits
    private const val GCM_NONCE_LENGTH = 12 // 96 bits
    private const val GCM_TAG_LENGTH = 128 // 16 bits

    private val secretKey: SecretKey by lazy {
        val keyBytes = System.getenv("AES_SECRET_KEY")?.toByteArray()
            ?: throw Exception("AES_SECRET_KEY environment variable not set.")
        SecretKeySpec(keyBytes.copyOf(AES_KEY_SIZE), "AES")
    }

    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val nonce = ByteArray(GCM_NONCE_LENGTH)
        SecureRandom().nextBytes(nonce)

        val spec = GCMParameterSpec(GCM_TAG_LENGTH, nonce)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val encrypted = nonce + cipherText
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(cipherTextBase64: String): String {
        val decoded = Base64.getDecoder().decode(cipherTextBase64)
        val nonce = decoded.copyOfRange(0, GCM_NONCE_LENGTH)
        val cipherText = decoded.copyOfRange(GCM_NONCE_LENGTH, decoded.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, nonce)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val plainTextBytes = cipher.doFinal(cipherText)
        return String(plainTextBytes, Charsets.UTF_8)
    }
}