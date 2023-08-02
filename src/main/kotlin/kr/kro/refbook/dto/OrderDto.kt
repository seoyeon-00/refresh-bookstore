package kr.kro.refbook.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import kr.kro.refbook.entities.tables.ShippingStatus
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderDto(
    val id: Int,

    @field:NotBlank
    @field:Email
    @JsonProperty("email")
    val email: String,

    @field:NotNull
    val shippingStatus: ShippingStatus,

    @field:NotNull
    @field:DecimalMin("0.00")
    val deliveryFee: BigDecimal,

    @field:NotBlank
    @field:Length(min = 1, max = 50)
    val userName: String,

    @field:NotBlank
    @field:Pattern(regexp = "^[0-9]{5}$")
    val postalCode: String,

    @field:NotBlank
    @field:Length(min = 1, max = 100)
    val address: String,

    @field:NotBlank
    @field:Length(min = 1, max = 100)
    val detailAddress: String,

    @field:NotBlank
    @field:Pattern(regexp = "^01(?:0|1|[6-9])-?(?:\\d{3}|\\d{4})-?\\d{4}$")
    val userPhone: String,

    @field:Length(max = 200)
    val orderRequest: String,

    @field:Length(min = 1, max = 50)
    val orderNumber: String?,

    @field:NotNull
    @field:DecimalMin("0.00")
    val totalPrice: BigDecimal,

    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,

    @field:NotEmpty
    val orderItems: List<OrderItemDto>,
)
