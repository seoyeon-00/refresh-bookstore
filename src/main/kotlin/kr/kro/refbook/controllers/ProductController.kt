package kr.kro.refbook.controllers

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.dto.PaginationDto
import kr.kro.refbook.dto.ProductWithPaginationDto
import kr.kro.refbook.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.ceil

@RestController
@RequestMapping("api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<ProductWithPaginationDto> {
        val products = productService.getAllProducts(page, size)
        val totalProducts = productService.getTotalProducts()

        // 클라이언트에게 전체 페이지 수를 전송
        val totalPages = kotlin.math.ceil(totalProducts.toDouble() / size).toInt()

        val paginationDto = PaginationDto(
            totalPages = totalPages,
            currentPage = page,
            pageSize = size,
            totalItems = totalProducts
        )

        return ResponseEntity.ok(ProductWithPaginationDto(products, paginationDto))
    }

    @GetMapping("/isbn/{isbn}")
    fun getProductByISBN(@PathVariable isbn: String): ResponseEntity<ProductDto> {
        return productService.getProductByISBN(isbn)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/category")
    fun getProductByCategory(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "1") category: Int
    ): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getProductByCategory(page, size, category))
    }

    @GetMapping("/category/total/{category}")
    fun getProductByCategory(@PathVariable category: Int): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getProductByCategoryAll(category))
    }

    // 검색 기능 - keyword로 책 조회하기
    @GetMapping("/search")
    fun getProductbyKeyword(@RequestParam keyword: String): ResponseEntity<List<ProductDto>> {
        return ResponseEntity.ok(productService.getProductbyKeyword(keyword))
    }

    @PostMapping
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return ResponseEntity.ok(productService.createProduct(productDto))
    }

    @PutMapping("/isbn/{isbn}")
    fun updateProduct(@PathVariable isbn: String, @RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        return productService.updateProduct(isbn, productDto)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/isbn/{isbn}")
    fun deleteProduct(@PathVariable isbn: String): ResponseEntity<Void> {
        return if (productService.deleteProduct(isbn)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
