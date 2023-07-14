package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Category
import kr.kro.refbook.entities.tables.Categories
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class CategoryRepository {

    init {
        transaction {
            SchemaUtils.create(Categories)
        }
    }

    fun findAll(): List<Category> = transaction {
        Category.all().toList()
    }

    fun findById(id: Int): Category? = transaction {
        Category.findById(id)
    }

    // categoryId 매개변수를 제거하고 name만 사용합니다.
    fun create(name: String): Category = transaction {
        Category.new {
            this.name = name
        }
    }

    // categoryId 매개변수를 제거하고 name만 사용합니다.
    fun update(id: Int, name: String): Category? = transaction {
        Category.findById(id)?.apply {
            this.name = name
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Category.findById(id)?.delete() != null
    }
}
