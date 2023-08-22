package kr.kro.refbook.utils

object MailServiceUtils {
    fun generateCertificationNumber(): String {
        return (10000..99999).random().toString()
    }

    fun generateTemporaryPassword(): String {
        val specialChars = "!@#"
        val charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val passwordLength = 15

        val randomSpecialChar = specialChars.random()
        val randomChars = (1..(passwordLength - 1)).map { charset.random() }

        return (listOf(randomSpecialChar) + randomChars).joinToString("")
    }
}