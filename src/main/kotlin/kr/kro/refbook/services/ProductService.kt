package kr.kro.refbook.services

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.entities.models.Category
import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.repositories.ProductRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(page: Int, size: Int): List<ProductDto> = transaction {
        productRepository.findAll(page, size).map { product ->
            toDto(product)
        }
    }

    // fun getTotalProducts(): List<ProductDto> {
    //     return transaction {
    //         productRepository.findAllProduct().map { it -> toDto(it) }
    //     }
    // }

    fun getTotalProducts(): Long {
        return transaction {
            Product.all().count()
        }
    }
    
    fun getProductByISBN(isbn: String): ProductDto? = transaction {
        productRepository.findByISBN(isbn)?.let { toDto(it) }
    }

    fun getProductByCategory(page: Int, size: Int, category: Int): List<ProductDto> = transaction {
        productRepository.findByCategory(page, size, category).map { product ->
            toDto(product)
        }
    }

    fun getProductByCategoryAll(category: Int): List<ProductDto> = transaction {
        productRepository.findByCategoryAll(category).map { product ->
            toDto(product)
        }
    }

    fun getProductbyKeyword(keyword: String): List<ProductDto> = transaction {
        productRepository.findByKeyword(keyword).map { toDto(it) }
    }

    fun createProduct(productDto: ProductDto): ProductDto = transaction {
        val category = Category.findById(productDto.categoryId)
            ?: throw IllegalArgumentException("카테고리 ${productDto.categoryId} 값을 찾을 수 없습니다.")

        productRepository.create(
            category.id.value,
            productDto.title,
            productDto.author,
            productDto.publisher,
            productDto.publicationDate,
            productDto.isbn,
            productDto.description,
            productDto.price,
            productDto.imagePath,
            productDto.isBestSeller,
        ).let { toDto(it) }
    }

    fun updateProduct(isbn: String, productDto: ProductDto): ProductDto? = transaction {
        val productToUpdate = productRepository.findByISBN(isbn)
            ?: throw IllegalArgumentException("$isbn 값의 상품이 없습니다.")

        productRepository.update(
            productToUpdate.id.value,
            productDto.title,
            productDto.author,
            productDto.publisher,
            productDto.publicationDate,
            productDto.isbn,
            productDto.description,
            productDto.price,
            productDto.imagePath,
            productDto.isBestSeller,
        )?.let { toDto(it) }
    }

    fun deleteProduct(isbn: String): Boolean = transaction {
        val productToDelete = productRepository.findByISBN(isbn)
            ?: throw IllegalArgumentException("$isbn 값의 상품이 없습니다.")

        productRepository.delete(productToDelete.id.value)
    }

    private fun toDto(product: Product): ProductDto =
        ProductDto(
            id = product.id.value,
            categoryId = product.category.id.value,
            title = product.title,
            author = product.author,
            publisher = product.publisher,
            publicationDate = product.publicationDate,
            isbn = product.isbn,
            description = product.description,
            price = product.price,
            imagePath = product.imagePath,
            isBestSeller = product.isBestSeller,
        )
}
