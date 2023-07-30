package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.entities.models.OrderItem
import kr.kro.refbook.repositories.OrderItemRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class OrderItemService(private val orderItemRepository: OrderItemRepository) {

    fun getAllOrderItems(): List<OrderItemDto> = transaction {
        orderItemRepository.findAll().map { orderList ->
            toDto(orderList)
        }
    }

    fun getOrderItemById(id: Int): OrderItemDto? = transaction {
        orderItemRepository.findById(id)?.let { toDto(it) }
    }

    fun updateOrderItem(id: Int, orderListDto: OrderItemDto): OrderItemDto? = transaction {
        orderItemRepository.update(id, orderListDto.amount)?.let { toDto(it) }
    }

    fun deleteOrderItem(id: Int): Boolean = transaction {
        orderItemRepository.delete(id)
    }

    private fun toDto(orderItem: OrderItem): OrderItemDto =
        OrderItemDto(
            id = orderItem.id.value,
            isbn = orderItem.product.isbn,
            amount = orderItem.amount,
            orderId = orderItem.order?.id?.value,
        )
}
