package kr.kro.refbook.repositories

import kr.kro.refbook.entities.Category
import kr.kro.refbook.entities.Categories
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository {

    fun findAll(): List<Category> = transaction {
        Category.all().toList()
    }

    fun findById(id: Int): Category? = transaction {
        Category.findById(id)
    }

    fun create(name: String, categoryId: Int): Category = transaction {
        Category.new {
            this.name = name
            this.categoryId = categoryId
        }
    }

    fun update(id: Int, name: String, categoryId: Int): Category? = transaction {
        Category.findById(id)?.apply {
            this.name = name
            this.categoryId = categoryId
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Category.findById(id)?.delete() != null
    }
}
