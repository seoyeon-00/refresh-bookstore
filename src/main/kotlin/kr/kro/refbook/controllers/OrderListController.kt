package kr.kro.refbook.controllers

import kr.kro.refbook.dto.OrderListDto
import kr.kro.refbook.services.OrderListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order-lists")
class OrderListController(private val orderListService: OrderListService) {

    @GetMapping
    fun getAllOrderLists(): ResponseEntity<List<OrderListDto>> {
        return ResponseEntity.ok(orderListService.getAllOrderLists())
    }

    @GetMapping("/{id}")
    fun getOrderListById(@PathVariable id: Int): ResponseEntity<OrderListDto> {
        return orderListService.getOrderListById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createOrderList(@RequestBody orderListDto: OrderListDto): ResponseEntity<OrderListDto> {
        return ResponseEntity.ok(orderListService.createOrderList(orderListDto))
    }

    @PutMapping("/{id}")
    fun updateOrderList(@PathVariable id: Int, @RequestBody orderListDto: OrderListDto): ResponseEntity<OrderListDto> {
        return orderListService.updateOrderList(id, orderListDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteOrderList(@PathVariable id: Int): ResponseEntity<Void> {
        return if (orderListService.deleteOrderList(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
