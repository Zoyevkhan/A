package com.tv9news.utils.helpers

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.tv9news.utils.helpers.SharedPreference.Companion.instance
import com.tv9news.utils.networks.ApiConstants
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES {
    private const val CIPHER_NAME = "AES/CBC/NoPadding"
    var strArrayKey = "MTA5MCMj1090##JSFGKiZeJClfKiUzZiZCKw==XWc7dnMnMmFs"
    var strArrayvector = "MTA5MCMj1090##IyokREp2eXcydyUhXy0kQA==XWc7dnMnMmFs"
    private const val CIPHER_KEY_LEN = 16 //128 bits

    fun encrypt(data: String): String {
        val key = generatekeyAPI()
        val iv = generateVectorAPI()
        return try {
            val ivSpec = IvParameterSpec(iv.toByteArray(charset("UTF-8")))
            val secretKey = SecretKeySpec(
                fixKey(key)!!.toByteArray(charset("UTF-8")), "AES"
            )
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val encryptedData = cipher.doFinal(data.toByteArray(charset("UTF-8")))
            val encryptedDataInBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
            "$encryptedDataInBase64:"
        } catch (ex: java.lang.Exception) {
            throw java.lang.RuntimeException(ex)
        }
    }

    fun decrypt(data: String, key: String, ivParameter: String): String {
        return try {
            if (data.contains(":")) {
                val parts = data.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val iv = IvParameterSpec(ivParameter.toByteArray())
                val secretKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
                val cipher = Cipher.getInstance(CIPHER_NAME)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
                val decodedEncryptedData = Base64.decode(parts[0], Base64.NO_PADDING)
                val original = cipher.doFinal(decodedEncryptedData)
                String(original)
            } else {
                val iv = IvParameterSpec(ivParameter.toByteArray())
                val secretKey = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
                val cipher = Cipher.getInstance(CIPHER_NAME)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
                val decodedEncryptedData = Base64.decode(data, Base64.NO_PADDING)
                val original = cipher.doFinal(decodedEncryptedData)
                String(original)
            }
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    fun generatekeyAPI(): String {
        var finalKey = ""
        val parts: String
        parts = if (instance != null && instance!!.loggedInUser != null && !TextUtils.isEmpty(
                instance!!.loggedInUser!!.id
            )
        ) {
            (instance!!.loggedInUser!!.id + ApiConstants.APITOKEN).substring(0, 16)
        } else {
            ("0" + ApiConstants.APITOKEN).substring(0, 16)
        }
        for (c in parts.toCharArray()) {
            finalKey = finalKey + encryptPassword(strArrayKey).toCharArray()[c.toString().toInt()]
        }
        return finalKey
    }

    fun generateVectorAPI(): String {
        var finalKey = ""
        val parts: String
        parts = if (instance != null && instance!!.loggedInUser != null && !TextUtils.isEmpty(
                instance!!.loggedInUser!!.id
            )
        ) {
            (instance!!.loggedInUser!!.id + ApiConstants.APITOKEN).substring(0, 16)
        } else {
            ("0" + ApiConstants.APITOKEN).substring(0, 16)
        }
        for (c in parts.toCharArray()) {
            finalKey =
                finalKey + encryptPassword(strArrayvector).toCharArray()[c.toString().toInt()]
        }
        return finalKey
    }

    fun encryptPassword(key: String): String {
        var password = ""
        val base = key.split("1090##".toRegex(), limit = 2).toTypedArray()
        val sub1 = base[0]
        val sub2 = base[1]
        val subBase = sub2.split("==".toRegex(), limit = 2).toTypedArray()
        val s1 = subBase[0]
        val finalEncodeStart = "$s1=="
        val decodeStart = Base64.decode(finalEncodeStart, Base64.DEFAULT)
        val finalDecodeStart = String(decodeStart, StandardCharsets.UTF_8)
        password = finalDecodeStart
        return password
    }

    private fun fixKey(key: String): String? {
        var key = key
        if (key.length < AES.CIPHER_KEY_LEN) {
            val numPad: Int = AES.CIPHER_KEY_LEN - key.length
            for (i in 0 until numPad) {
                key += "0" //0 pad to len 16 bytes
            }
            return key
        }
        return if (key.length > AES.CIPHER_KEY_LEN) {
            key.substring(0, AES.CIPHER_KEY_LEN) //truncate to 16 bytes
        } else key
    }
}