package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.User
import kr.kro.refbook.entities.models.OrderItem
import kr.kro.refbook.entities.tables.OrderItems
import kr.kro.refbook.entities.tables.Orders
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)

    var user by User referencedOn Orders.user
    var shippingStatus by Orders.shippingStatus
    var deliveryFee by Orders.deliveryFee
    var userName by Orders.userName
    var postalCode by Orders.postalCode
    var address by Orders.address
    var detailAddress by Orders.detailAddress
    var userPhone by Orders.userPhone
    var orderRequest by Orders.orderRequest
    var totalPrice by Orders.totalPrice
    var orderNumber by Orders.orderNumber
    var createdAt by Orders.createdAt
    var updatedAt by Orders.updatedAt

    val orderItems: List<OrderItem> get() = OrderItem.find { OrderItems.order eq id }.toList()
}

