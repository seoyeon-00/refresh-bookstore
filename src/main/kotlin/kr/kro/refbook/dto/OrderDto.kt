package kr.kro.refbook.dto

import kr.kro.refbook.entities.tables.ShippingStatus
import java.time.LocalDateTime

data class OrderDto(
    val id: Int,
    val email: String,
    val shippingStatus: ShippingStatus,
    val deliveryFee: Int,
    val userName: String,
    val postalCode: String,
    val address: String,
    val detailAddress: String,
    val userPhone: String,
    val orderRequest: String,
    val totalPrice: Int,
    val orderNumber: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val orderItems: List<OrderItemDto>
)
