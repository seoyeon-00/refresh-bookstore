package kr.kro.refbook.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class OrderItemDto(
    val id: Int,

    @field:NotBlank
    @field:Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$") // ISBN-10 or ISBN-13
    val isbn: String,

    val amount: Int,

    val orderId: Int?,
)
