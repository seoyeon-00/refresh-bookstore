package kr.kro.refbook.controllers

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.dto.PaginationDto
import kr.kro.refbook.dto.ProductWithPaginationDto
import kr.kro.refbook.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.ceil
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.nio.charset.StandardCharsets
import org.json.JSONArray
import org.json.JSONObject

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

        val isbn = productDto.isbn
        val openApiUrl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbreading941508001&Query=$isbn&Output=js"

        val (_, _, result) = openApiUrl
            .httpGet()
            .responseString(StandardCharsets.UTF_8)

        when (result) {
            is Result.Success -> {
                val openApiData = result.get()
                val imagePathFromApi = extractImagePathFromOpenApiData(openApiData)

                val updatedProductDto = productDto.copy(imagePath = imagePathFromApi)
                return ResponseEntity.ok(productService.createProduct(updatedProductDto))
            }
            is Result.Failure -> {
                println("Error while fetching OpenAPI data: ${result.error}")
                return ResponseEntity.status(500).build()
            }
        }
    }

    // @PutMapping("/isbn/{isbn}")
    // fun updateProduct(@PathVariable isbn: String, @RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
    //     return productService.updateProduct(isbn, productDto)?.let {
    //         ResponseEntity.ok(it)
    //     } ?: ResponseEntity.notFound().build()
    // }

    @PutMapping("/isbn/{isbn}")
    fun updateProduct(@PathVariable isbn: String, @RequestBody productDto: ProductDto): ResponseEntity<ProductDto> {
        
        val newIsbn = productDto.isbn
        val openApiUrl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbreading941508001&Query=$newIsbn&Output=js"

        val (_, _, result) = openApiUrl
            .httpGet()
            .responseString(StandardCharsets.UTF_8)

        when (result) {
            is Result.Success -> {
                val openApiData = result.get()
                val imagePathFromApi = extractImagePathFromOpenApiData(openApiData)

                val updatedProductDto = productDto.copy(imagePath = imagePathFromApi)
                return ResponseEntity.ok(productService.updateProduct(isbn, updatedProductDto))
            }
            is Result.Failure -> {
                println("Error while fetching OpenAPI data: ${result.error}")
                return ResponseEntity.status(500).build()
            }
        }

    }

    @DeleteMapping("/isbn/{isbn}")
    fun deleteProduct(@PathVariable isbn: String): ResponseEntity<Void> {
        return if (productService.deleteProduct(isbn)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    fun extractImagePathFromOpenApiData(openApiData: String): String {
        try {
            val jsonObject = JSONObject(openApiData)
            val itemsArray = jsonObject.getJSONArray("item")

            if (itemsArray.length() > 0) {
                val firstItem = itemsArray.getJSONObject(0)
                return firstItem.optString("cover", "https://example.com/image.jpg")
            }
        } catch (e: Exception) {
            println("OpenAPI 응답을 파싱하는 중 오류 발생: $e")
        }

        return "https://example.com/image.jpg"
    }
}
