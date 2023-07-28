package kr.kro.refbook.entities.tables

import kr.kro.refbook.entities.Users
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
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
    val deliveryFee: Column<Int> = integer("delivery_fee")
    val userName: Column<String> = varchar("user_name", 255)
    val postalCode: Column<String> = varchar("postal_code", 255)
    val address: Column<String> = text("address")
    val detailAddress: Column<String> = text("detail_address")
    val userPhone: Column<String> = varchar("user_phone", 15)
    val orderRequest: Column<String> = text("order_request")
    val totalPrice: Column<Int> = integer("total_price")
    val orderId: Column<String> = varchar("order_id", 36).default(UUID.randomUUID().toString())
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val updatedAt: Column<LocalDateTime> = datetime("updated_at").default(LocalDateTime.now())
}
