package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.OrderItem
import kr.kro.refbook.entities.tables.OrderItems
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

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

    fun create(isbn: String, amount: Int): OrderItem = transaction {
        val product = productRepository.findByISBN(isbn) ?: throw IllegalArgumentException("Product not found.")

        OrderItem.new {
            this.product = product
            this.amount = amount
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
