package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Product
import kr.kro.refbook.entities.tables.Products
// import org.jetbrains.exposed.sql.Op
// import org.jetbrains.exposed.sql.SchemaUtils
// import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import org.jetbrains.exposed.sql.selectAll
import kr.kro.refbook.dto.ProductPreviewDto

@Repository
@DependsOn("databaseConfig")
class ProductRepository(private val categoryRepository: CategoryRepository) {

    init {
        transaction {
            SchemaUtils.create(Products)
        }
    }

    fun findAll(page: Int, size: Int): List<Product> = transaction {
        Product.all()
            .limit(size, offset = (page * size).toLong())
            .toList()
    }

    fun findPreviewProduct(page: Int, size: Int): List<ProductPreviewDto> = transaction {
        Products.selectAll()
            .limit(size, offset = (page * size).toLong())
            .map {
                ProductPreviewDto(
                    it[Products.id].value,
                    it[Products.category].value,
                    it[Products.title],
                    it[Products.price],
                    it[Products.isbn],
                    it[Products.imagePath]
                )
            }
    }

    fun findAllProduct(): List<Product>{
        return transaction {
            Product.all().toList()
        }
    }

    fun findByISBN(isbn: String): Product? = transaction {
        Product.find { Products.isbn eq isbn }.singleOrNull()
    }

    fun findByCategory(page: Int, size: Int, category: Int): List<Product> = transaction {
        Product.find { Products.category eq category }
            .limit(size, offset = (page * size).toLong())
            .toList()
    }

    fun findByCategoryAll(category: Int): List<Product> = transaction {
        Product.find { Products.category eq category }.toList()
    }

    fun findByKeyword(keyword: String): List<Product> = transaction {
        Product.find {
            Op.build {
                (Products.title like "%$keyword%") or
                    (Products.author like "%$keyword%")
            }
        }.toList()
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
    ): Product? = transaction {
        
        val category = categoryRepository.findById(categoryId)
            ?: throw IllegalArgumentException("Invalid category ID")

        Product.findById(id)?.apply {
            this.title = title
            this.category = category
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
