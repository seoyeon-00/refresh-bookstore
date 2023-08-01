package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderDto
import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.repositories.OrderRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {

    fun getAllOrders(): List<OrderDto> = transaction {
        orderRepository.findAll().map { order ->
            toDto(order)
        }
    }

    fun getOrderById(id: Int): OrderDto? = transaction {
        toDto(orderRepository.findById(id))
    }


    fun getOrderByNumber(orderNumber: String): OrderDto? = transaction {
        orderRepository.findByNumber(orderNumber)?.let { toDto(it) }
    }

    fun createOrder(orderDto: OrderDto): OrderDto = transaction {
        orderRepository.create(
            orderDto.email,
            orderDto.shippingStatus,
            orderDto.deliveryFee,
            orderDto.userName,
            orderDto.postalCode,
            orderDto.address,
            orderDto.detailAddress,
            orderDto.userPhone,
            orderDto.orderRequest,
            orderDto.orderItems,
        ).let { toDto(it) }
    }

    fun updateOrder(id: Int, orderDto: OrderDto): OrderDto? = transaction {
        orderRepository.update(
            id,
            orderDto.shippingStatus,
            orderDto.deliveryFee,
            orderDto.userName,
            orderDto.postalCode,
            orderDto.address,
            orderDto.detailAddress,
            orderDto.userPhone,
            orderDto.orderRequest,
            orderDto.orderItems,
        )?.let { toDto(it) }
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
            orderNumber = order.orderNumber,
            totalPrice = order.totalPrice,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt,
            orderItems = order.orderItems.map { orderItem ->
                OrderItemDto(
                    id = orderItem.id.value,
                    isbn = orderItem.product.isbn,
                    amount = orderItem.amount,
                    orderId = orderItem.order?.id?.value,
                )
            },
        )
}
