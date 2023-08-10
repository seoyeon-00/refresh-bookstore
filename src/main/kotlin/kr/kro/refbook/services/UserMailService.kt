package kr.kro.refbook.services

import kr.kro.refbook.utils.RedisUtil
import kr.kro.refbook.utils.MailServiceUtils
import org.springframework.stereotype.Service
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.MailSendException
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.internet.MimeMessage
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@Service
class UserMailService(
    private val javaMailSender: JavaMailSender,
    private val redisUtil: RedisUtil,
) {

    fun sendMail(email: String) {
        val certificationNumber: String = MailServiceUtils.generateCertificationNumber()
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(email)
        helper.setSubject("[Refresh Bookstore] 본인 인증 메일")
        helper.setText(getHtmlText(certificationNumber), true)

        redisUtil.setDataExpire(certificationNumber, email, 5 * 60 * 1000)
        javaMailSender.send(message)
    }

    fun checkCertification(key: String) {
        redisUtil.getData(key)
            ?: throw MailSendException("인증번호가 잘못되었거나 인증 시간이 초과되었습니다. 다시 확인해주세요.")
        redisUtil.deleteData(key)
    }

    private fun getHtmlText(certificationNum: String): String {
        val resource = ClassPathResource("email-template.html")
        val htmlContent = FileCopyUtils.copyToString(
            InputStreamReader(resource.inputStream, StandardCharsets.UTF_8)
        ).replace("\${certificationNum}", certificationNum)

        return htmlContent
    }

}
