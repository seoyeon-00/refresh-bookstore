package kr.kro.refbook.controllers

import kr.kro.refbook.dto.OrderDto
import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.services.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping
    fun getAllOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<OrderDto>> {
        return ResponseEntity.ok(orderService.getAllOrders(page, size))
    }


    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id: Int): ResponseEntity<OrderDto> {
        return orderService.getOrderById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }


    @GetMapping("orderNumber/{orderNumber}")
    fun getOrderByNumber(@PathVariable orderNumber: String): ResponseEntity<OrderDto> {
        return orderService.getOrderByNumber(orderNumber)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }


    @PostMapping
    fun createOrder(@RequestBody orderDto: OrderDto): ResponseEntity<OrderDto> {
        if (orderDto.orderItems.isEmpty()) {
            throw IllegalArgumentException("적어도 하나 이상의 주문상품이 있어야 합니다.")
        }
        return ResponseEntity.ok(orderService.createOrder(orderDto))
    }


    @PutMapping("/{id}")
    fun updateOrder(@PathVariable id: Int, @RequestBody orderDto: OrderDto): ResponseEntity<OrderDto> {
        return orderService.updateOrder(id, orderDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Int): ResponseEntity<Void> {
        return if (orderService.deleteOrder(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
