package kr.kro.refbook.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

data class UserDto(
    var id: Int,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    val email: String,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요",
    )
    val password: String,

    @field:NotBlank
    @field:Length(min = 1, max = 5)
    val postalCode: String,

    @field:NotBlank
    @field:Length(min = 1, max = 100)
    val address: String,

    @field:NotNull
    val detailAddress: String,

    @field:NotBlank
    @field:Pattern(regexp = "^01(?:0|1|[6-9])[-]?(?:\\d{3}|\\d{4})[-]?\\d{4}$")
    val phone: String,

    val isAdmin: Boolean?,
    val createdAt: LocalDateTime?,

)

data class LoginDto(
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String,
)

data class UserDtoResponse(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val postalCode: String,
    val address: String,
    val detailAddress: String,
    val phone: String,
    val isAdmin: Boolean,
)

data class PasswordAuthenticationDto(
    @field:NotBlank
    val password: String,
)

data class CheckEmailRequestDto(
    @field:NotBlank
    @field:Email
    val email: String
)
