package crypto

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object MessageCryptoUtil {
    private const val defaultKey = "0123456789abcdef0123456789abcdef" // 32 bytes for AES-256
    private const val defaultIv = "abcdef9876543210" // 16 bytes for AES-CBC

    fun encrypt(plainText: String, key: String = defaultKey, iv: String = defaultIv): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.toByteArray())

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(base64CipherText: String, key: String = defaultKey, iv: String = defaultIv): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.toByteArray())

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        val decodedBytes = Base64.getDecoder().decode(base64CipherText)
        val decrypted = cipher.doFinal(decodedBytes)
        return String(decrypted)
    }
}