package kr.kro.refbook.dto

data class OrderListDto(
    val id: Int,
    val orderId: Int,
    val productId: Int,
    val amount: Int,
)
