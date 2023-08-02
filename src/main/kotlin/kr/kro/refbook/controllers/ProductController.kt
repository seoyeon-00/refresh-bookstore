package kr.kro.refbook.controllers

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getAllProducts(page, size))
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Int): ResponseEntity<ProductDto> {
        return productService.getProductById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/isbn/{isbn}")
    fun getProductByISBN(@PathVariable isbn: String): ResponseEntity<ProductDto> {
        return productService.getProductByISBN(isbn)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    // 검색 기능 :: keyword로 책 조회하기
    @GetMapping("/search")
    fun getProductbyKeyword(@RequestParam keyword: String): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getProductbyKeyword(keyword))
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
