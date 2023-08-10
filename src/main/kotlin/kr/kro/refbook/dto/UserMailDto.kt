package kr.kro.refbook.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CertificationRequestDto(
    @field:NotBlank
    @field:Email
    val email: String
)

data class CertificationCheckRequestDto(
    @field:NotBlank
    val key: String
)
