package kr.kro.refbook.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import kr.kro.refbook.entities.tables.ShippingStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderDto(
    val id: Int,

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    val email: String,


    val shippingStatus: ShippingStatus,
    val deliveryFee: BigDecimal,
    val userName: String,
    val postalCode: String,
    val address: String,
    val detailAddress: String,
    val userPhone: String,
    val orderRequest: String,
    val orderNumber: String?,
    val totalPrice: BigDecimal,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val orderItems: List<OrderItemDto>,
)
