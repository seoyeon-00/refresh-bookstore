package kr.kro.refbook.services

import kr.kro.refbook.dto.OrderDto
import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.entities.models.Order
import kr.kro.refbook.repositories.OrderRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import kr.kro.refbook.entities.tables.ShippingStatus

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import org.springframework.mail.javamail.MimeMessageHelper
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val javaMailSender: JavaMailSender,
) {

    fun getAllOrders(page: Int, size: Int): List<OrderDto> = transaction {
        orderRepository.findAll(page, size).map { order ->
            toDto(order)
        }
    }

    fun getOrderById(id: Int): OrderDto? = transaction {
        toDto(orderRepository.findById(id))
    }


    fun getOrderByNumber(orderNumber: String): OrderDto? = transaction {
        orderRepository.findByNumber(orderNumber)?.let { toDto(it) }
    }

    suspend fun createOrder(orderDto: OrderDto): OrderDto? = coroutineScope {
        transaction {
            val email = orderDto.email ?: ""
            val name = orderDto.userName.ifBlank { "회원" }
            
            val createdOrder = orderRepository.create(
                orderDto.email!!,
                orderDto.shippingStatus,
                orderDto.deliveryFee,
                orderDto.userName,
                orderDto.postalCode,
                orderDto.address,
                orderDto.detailAddress,
                orderDto.userPhone,
                orderDto.orderRequest,
                orderDto.orderItems,
            )

            launch {
                val message = javaMailSender.createMimeMessage()
                val helper = MimeMessageHelper(message, true)
                helper.setTo(email)
                helper.setSubject("[Refresh Bookstore] ${name}님의 주문이 접수되었습니다.")
                val htmlTemplate = "email-Preparing.html"
                helper.setText(getHtmlText(htmlTemplate), true)
                javaMailSender.send(message)
            }
            toDto(createdOrder)
        }
    }

    suspend fun updateOrder(id: Int, orderDto: OrderDto): OrderDto? = coroutineScope {
        transaction {
            val existingOrder = orderRepository.findById(id)
            val email = orderDto.email ?: ""
            val name = orderDto.userName.ifBlank { "회원" }
            val previousShippingStatus = existingOrder.shippingStatus

            val updatedOrder = orderRepository.update(
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
            )

            if (previousShippingStatus != updatedOrder?.shippingStatus) {
                launch {
                    val message = javaMailSender.createMimeMessage()
                    val helper = MimeMessageHelper(message, true)
                    helper.setTo(email)
                    helper.setSubject("[Refresh Bookstore] 🚀${name}님! 배송 안내 메일이 도착했습니다.")

                    val htmlTemplate = when (updatedOrder?.shippingStatus) {
                        ShippingStatus.SHIPPING -> "email-Shipping.html"
                        ShippingStatus.COMPLETED -> "email-Completed.html"
                        ShippingStatus.CANCELLED -> "email-Cancelled.html"
                        else -> "email-Preparing.html"
                    }

                    helper.setText(getHtmlText(htmlTemplate), true)
                    javaMailSender.send(message)
                }
            }
            updatedOrder?.let { toDto(it) }
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

    private fun getHtmlText(htmlTemplate: String): String {
        val resource = ClassPathResource(htmlTemplate)
        return FileCopyUtils.copyToString(
            InputStreamReader(resource.inputStream, StandardCharsets.UTF_8)
        )
    }

}
