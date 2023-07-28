package kr.kro.refbook.services

import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.entities.models.Category
import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.repositories.ProductRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<ProductDto> = transaction {
        productRepository.findAll().map { product ->
            toDto(product)
        }
    }

    fun getProductById(id: Int): ProductDto? = transaction {
        productRepository.findById(id)?.let { toDto(it) }
    }

    fun getProductByISBN(isbn: String): ProductDto? = transaction {
        productRepository.findByISBN(isbn)?.let { toDto(it) }
    }

    fun getProductbyKeyword(keyword: String): List<ProductDto> = transaction {
        productRepository.findByKeyword(keyword).map { toDto(it) }
    }

    fun createProduct(productDto: ProductDto): ProductDto = transaction {
        val category = Category.findById(productDto.categoryId)
            ?: throw IllegalArgumentException("No category with id ${productDto.categoryId} found.")

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

    fun updateProduct(id: Int, productDto: ProductDto): ProductDto? = transaction {
        val productToUpdate = productRepository.findById(id)
            ?: throw IllegalArgumentException("No product with id $id found.")

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

    fun deleteProduct(id: Int): Boolean = transaction {
        val productToDelete = productRepository.findById(id)
            ?: throw IllegalArgumentException("No product with id $id found.")

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
