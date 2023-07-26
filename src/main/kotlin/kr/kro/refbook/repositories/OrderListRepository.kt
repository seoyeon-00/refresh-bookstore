package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.entities.models.OrderList
import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.entities.tables.OrderLists
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class OrderListRepository {

    init {
        transaction {
            SchemaUtils.create(OrderLists)
        }
    }

    fun findAll(): List<OrderList> = transaction {
        OrderList.all().toList()
    }

    fun findById(id: Int): OrderList? = transaction {
        OrderList.findById(id)
    }

    fun create(orderId: Int, productId: Int, amount: Int): OrderList = transaction {
        val order = Order.findById(orderId)
        val product = Product.findById(productId)

        if (order == null || product == null) {
            throw IllegalArgumentException("Order or Product not found.")
        }

        OrderList.new {
            this.order = order
            this.product = product
            this.amount = amount
        }
    }

    fun update(id: Int, amount: Int): OrderList? = transaction {
        OrderList.findById(id)?.apply {
            this.amount = amount
        }
    }

    fun delete(id: Int): Boolean = transaction {
        OrderList.findById(id)?.delete() != null
    }
}
