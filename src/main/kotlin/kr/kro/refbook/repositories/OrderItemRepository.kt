package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.OrderItem
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.entities.tables.OrderItems
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class OrderItemRepository(private val productRepository: ProductRepository) {

    init {
        transaction {
            SchemaUtils.create(OrderItems)
        }
    }

    fun findAll(): List<OrderItem> = transaction {
        OrderItem.all().toList()
    }

    fun findById(id: Int): OrderItem? = transaction {
        OrderItem.findById(id)
    }

    fun create(orderId: Int, isbn: String, amount: Int): OrderItem = transaction {
        val product = productRepository.findByISBN(isbn) ?: throw IllegalArgumentException("Product not found.")
        val order = Order.findById(orderId) ?: throw IllegalArgumentException("Order not found.")

        OrderItem.new {
            this.product = product
            this.amount = amount
            this.order = order
        }
    }

    fun update(id: Int, amount: Int): OrderItem? = transaction {
        OrderItem.findById(id)?.apply {
            this.amount = amount
        }
    }

    fun delete(id: Int): Boolean = transaction {
        OrderItem.findById(id)?.delete() != null
    }
}
