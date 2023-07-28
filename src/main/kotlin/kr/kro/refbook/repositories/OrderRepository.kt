package kr.kro.refbook.repositories

import kr.kro.refbook.entities.User
import kr.kro.refbook.entities.Users
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.entities.tables.Orders
import kr.kro.refbook.entities.tables.ShippingStatus
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class OrderRepository {

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

    fun create(email: String, shippingStatus: ShippingStatus, totalPrice: Int): Order = transaction {
        val user = User.find { Users.email eq email }.singleOrNull() ?: throw IllegalArgumentException("User not found.")

        Order.new {
            this.user = user
            this.shippingStatus = shippingStatus
            this.totalPrice = totalPrice
        }
    }

    fun update(id: Int, shippingStatus: ShippingStatus): Order? = transaction {
        Order.findById(id)?.apply {
            this.shippingStatus = shippingStatus
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Order.findById(id)?.delete() != null
    }
}
