package kr.kro.refbook.controllers

import org.springframework.http.ResponseEntity
import kr.kro.refbook.dto.CertificationRequestDto
import kr.kro.refbook.dto.CertificationCheckRequestDto
import org.springframework.web.bind.annotation.*
import kr.kro.refbook.services.UserMailService
import kr.kro.refbook.common.dto.BaseResponse

@RestController
@RequestMapping("/api/certify")
class UserMailController(private val userMailService: UserMailService) {

  @PostMapping("/email")
  fun sendEmailCertification(@RequestBody certificationRequestDto: CertificationRequestDto): BaseResponse<String> {
      userMailService.sendMail(certificationRequestDto.email)
      return BaseResponse(message = "Send Email Successfully")
  }
  
  @PostMapping("/check")
  fun checkEmailCertification(@RequestBody certificationCheckRequestDto: CertificationCheckRequestDto): BaseResponse<String> {
      userMailService.checkCertification(certificationCheckRequestDto.key)
      return BaseResponse(message = "Email verification is complete")
  }

}