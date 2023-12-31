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
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.math.BigDecimal

@Repository
@DependsOn("databaseConfig")
class OrderRepository(private val userRepository: UserRepository, private val orderItemRepository: OrderItemRepository, private val productRepository: ProductRepository) {

    companion object {
        private val FREE_SHIPPING_THRESHOLD = BigDecimal(50000)
    }


    init {
        transaction {
            SchemaUtils.create(Orders)
        }
    }

    private fun calculateTotalPrice(orderItemsDto: List<OrderItemDto>, deliveryFee: BigDecimal): Pair<BigDecimal, BigDecimal> {
        var itemTotal = BigDecimal.ZERO
        orderItemsDto.forEach { orderItemDto ->
            val product = productRepository.findByISBN(orderItemDto.isbn)
                ?: throw ProductNotFoundException("해당 상품을 찾을 수 없습니다.")
            itemTotal += product.price * BigDecimal(orderItemDto.amount)
        }

        val totalPrice = if (itemTotal >= FREE_SHIPPING_THRESHOLD) itemTotal else itemTotal + deliveryFee

        return Pair(itemTotal, totalPrice)
    }


    fun findAll(page: Int, size: Int): List<Order> = transaction {
        Order.all()
            .limit(size, offset = (page * size).toLong())
            .toList()
    }

    fun findById(id: Int): Order = transaction {
        Order.findById(id) ?: throw IllegalArgumentException("주문 $id 를 찾을 수 없습니다.")
    }

    fun findByUser(user: Int): List<Order> = transaction {
        Order.find { Orders.user eq user }.toList() 
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
        val customAlphabet: CharArray = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

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
            this.orderNumber = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, customAlphabet, 10)
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

            // Fetch the order items using findByOrderId method
            val existingOrderItems = orderItemRepository.findByOrderId(id)

            // Handle existing order items.
            existingOrderItems.forEach { existingItem ->
                val matchingDto = orderItemsDto.find { it.isbn == existingItem.product.isbn }
                if (matchingDto != null) {
                    // 기존 item을 수정합니다.
                    existingItem.amount = matchingDto.amount
                } else {
                    // 기존 아이템을 삭제합니다.
                    existingItem.delete()
                }
            }

            // 새로운 아이템을 수정합니다.
            orderItemsDto.forEach { dto ->
                if (existingOrderItems.none { it.product.isbn == dto.isbn }) {
                    orderItemRepository.create(this.id.value, dto.isbn, dto.amount)
                }
            }

            val (itemTotal, totalPrice) = calculateTotalPrice(orderItemsDto, deliveryFee)
            this.deliveryFee = if (itemTotal >= BigDecimal(50000)) BigDecimal.ZERO else deliveryFee
            this.totalPrice = totalPrice
        } ?: throw OrderNotFoundException("주문 $id 를 찾을 수 없습니다.")
    }


    fun delete(id: Int): Boolean = transaction {
        Order.findById(id)?.delete() != null
    }

    class ProductNotFoundException(message: String) : IllegalArgumentException(message)
    class OrderNotFoundException(message: String) : IllegalArgumentException(message)
}
