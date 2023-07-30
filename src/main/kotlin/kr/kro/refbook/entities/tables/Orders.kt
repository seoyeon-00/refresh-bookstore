package kr.kro.refbook.entities.tables

import kr.kro.refbook.entities.MemberRole.Companion.referrersOn
import kr.kro.refbook.entities.Users
import kr.kro.refbook.entities.models.OrderItem
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

enum class ShippingStatus(val status: Int) {
    PREPARING(0),
    SHIPPING(1),
    COMPLETED(2),
    CANCELLED(3),
}

object Orders : IntIdTable() {
    val user: Column<EntityID<Int>> = reference("user_id", Users)
    val shippingStatus: Column<ShippingStatus> = enumeration("shipping_status", ShippingStatus::class)
    val deliveryFee: Column<BigDecimal> = decimal("delivery_fee", precision = 10, scale = 2)
    val userName: Column<String> = varchar("user_name", 255)
    val postalCode: Column<String> = varchar("postal_code", 255)
    val address: Column<String> = text("address")
    val detailAddress: Column<String> = text("detail_address")
    val userPhone: Column<String> = varchar("user_phone", 15)
    val orderRequest: Column<String> = text("order_request")
    val totalPrice: Column<BigDecimal> = decimal("total_price", precision = 10, scale = 2)
    val orderNumber: Column<String> = varchar("order_id", 36).default(UUID.randomUUID().toString())
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val updatedAt: Column<LocalDateTime> = datetime("updated_at").default(LocalDateTime.now())
}
