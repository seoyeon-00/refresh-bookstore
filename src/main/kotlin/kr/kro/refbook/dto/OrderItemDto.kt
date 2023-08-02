package kr.kro.refbook.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class OrderItemDto(
    val id: Int,

    @field:NotBlank
    @field:Pattern(regexp = "^(97([89]))?\\d{9}(\\d|X)$") // ISBN Validation
    val isbn: String,

    @field:Min(value = 1, message = "수량은 1이상 이어야 합니다.")
    val amount: Int,

    val orderId: Int?,
)
