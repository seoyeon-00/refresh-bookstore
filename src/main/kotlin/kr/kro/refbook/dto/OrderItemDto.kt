package kr.kro.refbook.dto

data class OrderItemDto(
    val id: Int,
    val isbn: String,
    val amount: Int,
    val orderId: Int?,
)
