package com.oguzhanaslann.util

import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


class SecurityManager {

    fun hash(input: String, keyLength: Int = KEY_LENGTH): String {
        val salt = ByteArray(16)
        val spec: KeySpec = PBEKeySpec(input.toCharArray(), salt, ITERATION_COUNT, keyLength)
        val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val hash = f.generateSecret(spec).encoded
        val enc: Base64.Encoder = Base64.getEncoder()
        return enc.encodeToString(hash)
    }

    companion object {
        private const val ITERATION_COUNT = 65536
        private const val KEY_LENGTH = 128
    }
}