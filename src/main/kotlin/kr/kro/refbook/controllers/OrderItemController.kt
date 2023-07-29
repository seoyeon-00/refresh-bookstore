package kr.kro.refbook.controllers

import kr.kro.refbook.dto.OrderItemDto
import kr.kro.refbook.services.OrderItemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order-lists")
class OrderItemController(private val orderItemService: OrderItemService) {

    @GetMapping
    fun getAllOrderItems(): ResponseEntity<List<OrderItemDto>> {
        return ResponseEntity.ok(orderItemService.getAllOrderItems())
    }

    @GetMapping("/{id}")
    fun getOrderItemById(@PathVariable id: Int): ResponseEntity<OrderItemDto> {
        return orderItemService.getOrderItemById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createOrderItem(@RequestBody orderItemDto: OrderItemDto): ResponseEntity<OrderItemDto> {
        return ResponseEntity.ok(orderItemService.createOrderItem(orderItemDto))
    }

    @PutMapping("/{id}")
    fun updateOrderItem(@PathVariable id: Int, @RequestBody orderItemDto: OrderItemDto): ResponseEntity<OrderItemDto> {
        return orderItemService.updateOrderItem(id, orderItemDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteOrderList(@PathVariable id: Int): ResponseEntity<Void> {
        return if (orderItemService.deleteOrderItem(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
