package kr.kro.refbook.services

import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.dto.ProductDto
import kr.kro.refbook.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<ProductDto> =
        productRepository.findAll().map { product ->
            toDto(product)
        }

    fun getProductById(id: Int): ProductDto? =
        productRepository.findById(id)?.let { toDto(it) }

    fun createProduct(productDto: ProductDto): ProductDto =
        productRepository.create(
            productDto.categoryId,
            productDto.title,
            productDto.author,
            productDto.publisher,
            productDto.publicationDate,
            productDto.isbn,
            productDto.description,
            productDto.price,
            productDto.imagePath,
            productDto.isBestSeller
        ).let { toDto(it) }

    fun updateProduct(id: Int, productDto: ProductDto): ProductDto? =
        productRepository.update(
            id,
            productDto.title,
            productDto.author,
            productDto.publisher,
            productDto.publicationDate,
            productDto.isbn,
            productDto.description,
            productDto.price,
            productDto.imagePath,
            productDto.isBestSeller
        )?.let { toDto(it) }

    fun deleteProduct(id: Int): Boolean = productRepository.delete(id)

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
            isBestSeller = product.isBestSeller
        )
}
