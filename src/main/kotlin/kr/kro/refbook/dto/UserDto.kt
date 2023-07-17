package kr.kro.refbook.dto

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Email
import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
  val id: Int, 

  @field:NotBlank
  val name: String,

  @field:NotBlank
  @field:Email
  @JsonProperty("email")
  val email: String,

  @field:NotBlank
  @field:Pattern(
      regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
      message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
  )
  val password: String,

  @field:NotBlank
  val postalCode: String,

  @field:NotBlank
  val address: String,

  @field:NotBlank
  val detailAddress: String,

  @field:NotBlank
  val phone: String,

  val isAdmin: Boolean?,
  val createdAt: LocalDateTime?

) 

data class LoginDto (
  @field:NotBlank
  val email: String,

  @field:NotBlank
  val password: String,
)

data class UserDtoResponse(
    val id: Int,
    val name: String,
    val email: String,
    val postalCode: String,
    val address: String,
    val detailAddress: String,
    val phone: String,
)
