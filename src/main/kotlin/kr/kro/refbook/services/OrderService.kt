package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderDto
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.repositories.OrderRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class OrderService(private val orderRepository: OrderRepository) {

    fun getAllOrders(): List<OrderDto> = transaction {
        orderRepository.findAll().map { order ->
            toDto(order)
        }
    }

    fun getOrderById(id: Int): OrderDto? = transaction {
        orderRepository.findById(id)?.let { toDto(it) }
    }

    fun createOrder(orderDto: OrderDto): OrderDto = transaction {
        orderRepository.create(
            orderDto.email,
            orderDto.shippingStatus,
            orderDto.totalPrice,
        ).let { toDto(it) }
    }

    fun updateOrder(id: Int, orderDto: OrderDto): OrderDto? = transaction {
        orderRepository.update(id, orderDto.shippingStatus)?.let { toDto(it) }
    }

    fun deleteOrder(id: Int): Boolean = transaction {
        orderRepository.delete(id)
    }

    private fun toDto(order: Order): OrderDto =
        OrderDto(
            id = order.id.value,
            email = order.user.email,
            shippingStatus = order.shippingStatus,
            deliveryFee = order.deliveryFee,
            userName = order.userName,
            postalCode = order.postalCode,
            address = order.address,
            detailAddress = order.detailAddress,
            userPhone = order.userPhone,
            orderRequest = order.orderRequest,
            totalPrice = order.totalPrice,
            orderId = order.orderId,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt,
        )
}
