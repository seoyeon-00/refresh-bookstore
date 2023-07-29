package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderDto
import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.repositories.OrderRepository
import kr.kro.refbook.repositories.OrderItemRepository
import kr.kro.refbook.repositories.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) {

    fun getAllOrders(): List<OrderDto> = transaction {
        orderRepository.findAll().map { order ->
            toDto(order)
        }
    }

    fun getOrderById(id: Int): OrderDto? = transaction {
        toDto(orderRepository.findById(id))
    }

    fun createOrder(orderDto: OrderDto): OrderDto? {
        return try {
            transaction {

                val createdOrder = orderRepository.create(
                    orderDto.email,
                    orderDto.shippingStatus,
                    orderDto.totalPrice,
                )


                orderDto.orderItems.forEach {
                    orderItemDto -> orderItemRepository.create(orderItemDto.isbn, orderItemDto.amount)
                }

                val dto = toDto(createdOrder) // 'return' keyword is not necessary here.
                dto
            }
        } catch (e: Exception) {
            // 오류를 로깅하고 null을 반환하거나, 필요하다면 사용자 정의 예외를 발생시킵니다.
            println("An error occurred: ${e.message}")
            null
        }
    }

    fun updateOrder(id: Int, orderDto: OrderDto): OrderDto? = transaction {
        orderRepository.update(id, orderDto.shippingStatus)?.let { updatedOrder ->
            // update order items if provided in the DTO
            orderDto.orderItems.forEach { orderItemDto ->
                orderItemRepository.update(orderItemDto.id, orderItemDto.amount)
            }
            toDto(updatedOrder)
        }
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
            orderItems = order.orderItems.map { orderItem -> OrderItemDto(
                id = orderItem.id.value,
                isbn = orderItem.product.isbn,
                amount = orderItem.amount,
                orderId = orderItem.order?.id?.value
               )
            }
        )
}
