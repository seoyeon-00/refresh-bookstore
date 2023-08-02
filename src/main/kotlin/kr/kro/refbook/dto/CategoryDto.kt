package kr.kro.refbook.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class CategoryDto(
    val id: Int,

    @field:NotBlank
    @field:Length(min = 1, max = 50)
    val name: String,
)
