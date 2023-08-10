package kr.kro.refbook.utils

object MailServiceUtils {
    fun generateCertificationNumber(): String {
        return (10000..99999).random().toString()
    }
}