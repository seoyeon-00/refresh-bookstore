package kr.kro.refbook.repositories

import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.entities.tables.Orders
import kr.kro.refbook.entities.tables.Products
import kr.kro.refbook.entities.tables.ShippingStatus
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
@DependsOn("databaseConfig")
class OrderRepository(private val userRepository: UserRepository, private val orderItemRepository: OrderItemRepository, private val productRepository: ProductRepository) {

    init {
        transaction {
            SchemaUtils.create(Orders)
        }
    }

    private fun calculateTotalPrice(orderItemsDto: List<OrderItemDto>, deliveryFee: BigDecimal): Pair<BigDecimal, BigDecimal> {
        var itemTotal = BigDecimal.ZERO
        orderItemsDto.forEach { orderItemDto ->
            val product = productRepository.findByISBN(orderItemDto.isbn)
                ?: throw IllegalArgumentException("Product not found.")
            itemTotal += product.price * BigDecimal(orderItemDto.amount)
        }

        val totalPrice = if (itemTotal >= BigDecimal(50000)) {
            itemTotal
        } else {
            itemTotal + deliveryFee
        }

        return Pair(itemTotal, totalPrice)
    }


    fun findAll(): List<Order> = transaction {
        Order.all().toList()
    }

    fun findById(id: Int): Order = transaction {
        Order.findById(id) ?: throw IllegalArgumentException("Order with ID: $id not found.")
    }

    fun findByNumber(orderNumber: String): Order? = transaction {
        Order.find { Orders.orderNumber eq orderNumber }.singleOrNull()
    }

    fun create(
        userEmail: String,
        shippingStatus: ShippingStatus,
        deliveryFee: BigDecimal,
        userName: String,
        postalCode: String,
        address: String,
        detailAddress: String,
        userPhone: String,
        orderRequest: String,
        orderItemsDto: List<OrderItemDto>,
    ): Order = transaction {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("Invalid user email")

        val (itemTotal, totalPrice) = calculateTotalPrice(orderItemsDto, deliveryFee)

        Order.new {
            this.user = user
            this.shippingStatus = shippingStatus
            this.deliveryFee = if (itemTotal >= BigDecimal(50000)) BigDecimal.ZERO else deliveryFee
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
        deliveryFee: BigDecimal,
        userName: String,
        postalCode: String,
        address: String,
        detailAddress: String,
        userPhone: String,
        orderRequest: String,
        orderItemsDto: List<OrderItemDto>,
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

            // clear existing order items
            orderItems.forEach { it.delete() }

            val (itemTotal, totalPrice) = calculateTotalPrice(orderItemsDto, deliveryFee)
            this.deliveryFee = if (itemTotal >= BigDecimal(50000)) BigDecimal.ZERO else deliveryFee
            this.totalPrice = totalPrice
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Order.findById(id)?.delete() != null
    }
}
