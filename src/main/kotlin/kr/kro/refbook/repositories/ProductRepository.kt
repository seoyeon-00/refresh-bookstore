package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.entities.tables.Products
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
class ProductRepository(private val categoryRepository: CategoryRepository) {

    init {
        transaction {
            SchemaUtils.create(Products)
        }
    }

    fun findAll(): List<Product> = transaction {
        Product.all().toList()
    }

    fun findById(id: Int): Product? = transaction {
        Product.findById(id)
    }

    fun create(
        categoryId: Int,
        title: String,
        author: String,
        publisher: String,
        publicationDate: LocalDate,
        isbn: String,
        description: String,
        price: BigDecimal,
        imagePath: String,
        isBestSeller: Boolean,
    ): Product = transaction {
        val category = categoryRepository.findById(categoryId)
            ?: throw IllegalArgumentException("Invalid category ID")

        println("Category: $category")

        Product.new {
            this.category = category
            this.title = title
            this.author = author
            this.publisher = publisher
            this.publicationDate = publicationDate
            this.isbn = isbn
            this.description = description
            this.price = price
            this.imagePath = imagePath
            this.isBestSeller = isBestSeller
        }.also {
            println("Product: $it")
        }
    }

    fun update(
        id: Int,
        title: String,
        author: String,
        publisher: String,
        publicationDate: LocalDate,
        isbn: String,
        description: String,
        price: BigDecimal,
        imagePath: String,
        isBestSeller: Boolean,
    ): Product? = transaction {
        Product.findById(id)?.apply {
            this.title = title
            this.author = author
            this.publisher = publisher
            this.publicationDate = publicationDate
            this.isbn = isbn
            this.description = description
            this.price = price
            this.imagePath = imagePath
            this.isBestSeller = isBestSeller
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Product.findById(id)?.delete() != null
    }
}
