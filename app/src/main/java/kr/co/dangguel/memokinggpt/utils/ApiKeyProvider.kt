package kr.co.dangguel.memokinggpt.util

import android.content.Context
import kr.co.dangguel.memokinggpt.BuildConfig
import kr.co.dangguel.memokinggpt.utils.EncryptionUtil
import java.io.BufferedReader
import java.io.InputStreamReader

object ApiKeyProvider {
    fun getApiKey(context: Context): String {
        return try {
            val inputStream = context.assets.open("api_key.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val encryptedKey = reader.readLine()
            reader.close()
            val decryptedKey = EncryptionUtil.decrypt(
                BuildConfig.DECRYPTION_KEY,
                BuildConfig.DECRYPTION_SALT,
                encryptedKey,
            )
            decryptedKey ?: throw Exception("Decryption failed or API Key is empty.")
        } catch (e: Exception) {
            throw Exception("API Key read or decrypt failed: ${e.message}")
        }
    }
}
