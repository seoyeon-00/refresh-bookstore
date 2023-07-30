package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.entities.tables.Orders
import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.entities.tables.ShippingStatus
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class OrderRepository(private val userRepository: UserRepository, private val orderItemRepository: OrderItemRepository) {

    init {
        transaction {
            SchemaUtils.create(Orders)
        }
    }

    fun findAll(): List<Order> = transaction {
        Order.all().toList()
    }

    fun findById(id: Int): Order = transaction {
        Order.findById(id) ?: throw IllegalArgumentException("Order with ID: $id not found.")
    }

    fun create(
        userEmail: String,
        shippingStatus: ShippingStatus,
        deliveryFee: Int,
        userName: String,
        postalCode: String,
        address: String,
        detailAddress: String,
        userPhone: String,
        orderRequest: String,
        totalPrice: Int,
        orderItemsDto: List<OrderItemDto>
    ): Order = transaction {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("Invalid user email")

        Order.new {
            this.user = user
            this.shippingStatus = shippingStatus
            this.deliveryFee = deliveryFee
            this.userName = userName
            this.postalCode = postalCode
            this.address = address
            this.detailAddress = detailAddress
            this.userPhone = userPhone
            this.orderRequest = orderRequest
            this.totalPrice = totalPrice
        }.also { order ->
            orderItemsDto.forEach { orderItemDto ->
                orderItemRepository.create(order.id.value, orderItemDto.isbn, orderItemDto.amount)
            }
        }
    }


    fun update(
        id: Int,
        shippingStatus: ShippingStatus,
        deliveryFee: Int,
        userName: String,
        postalCode: String,
        address: String,
        detailAddress: String,
        userPhone: String,
        orderRequest: String,
        totalPrice: Int,
        orderItemsDto: List<OrderItemDto>
    ): Order? = transaction {
        Order.findById(id)?.apply {
            this.shippingStatus = shippingStatus
            this.deliveryFee = deliveryFee
            this.userName = userName
            this.postalCode = postalCode
            this.address = address
            this.detailAddress = detailAddress
            this.userPhone = userPhone
            this.orderRequest = orderRequest
            this.totalPrice = totalPrice
        }?.also { order ->
            // clear existing order items
            order.orderItems.forEach { it.delete() }

            // create new order items
            orderItemsDto.forEach { orderItemDto ->
                orderItemRepository.create(order.id.value, orderItemDto.isbn, orderItemDto.amount)
            }
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Order.findById(id)?.delete() != null
    }
}
