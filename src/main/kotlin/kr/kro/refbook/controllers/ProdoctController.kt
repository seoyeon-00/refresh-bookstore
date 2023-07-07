package kr.kro.refbook.controllers

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getAllProducts())
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Int): ResponseEntity<ProductDto> {
        return productService.getProductById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity.ok(productService.createProduct(productDto))
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Int, @RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return productService.updateProduct(id, productDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Int): ResponseEntity<Void> {
        return if (productService.deleteProduct(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
