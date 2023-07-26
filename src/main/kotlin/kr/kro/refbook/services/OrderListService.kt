package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderListDto
import kr.kro.refbook.entities.models.OrderList
import kr.kro.refbook.repositories.OrderListRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class OrderListService(private val orderListRepository: OrderListRepository) {

    fun getAllOrderLists(): List<OrderListDto> = transaction {
        orderListRepository.findAll().map { orderList ->
            toDto(orderList)
        }
    }

    fun getOrderListById(id: Int): OrderListDto? = transaction {
        orderListRepository.findById(id)?.let { toDto(it) }
    }

    fun createOrderList(orderListDto: OrderListDto): OrderListDto = transaction {
        orderListRepository.create(
            orderListDto.orderId,
            orderListDto.productId,
            orderListDto.amount,
        ).let { toDto(it) }
    }

    fun updateOrderList(id: Int, orderListDto: OrderListDto): OrderListDto? = transaction {
        orderListRepository.update(id, orderListDto.amount)?.let { toDto(it) }
    }

    fun deleteOrderList(id: Int): Boolean = transaction {
        orderListRepository.delete(id)
    }

    private fun toDto(orderList: OrderList): OrderListDto =
        OrderListDto(
            id = orderList.id.value,
            orderId = orderList.order.id.value,
            productId = orderList.product.id.value,
            amount = orderList.amount,
        )
}
