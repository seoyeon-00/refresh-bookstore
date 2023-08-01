package kr.kro.refbook.repositories

import kr.kro.refbook.entities.models.Category
import kr.kro.refbook.entities.tables.Categories
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Repository

@Repository
@DependsOn("databaseConfig")
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

    fun create(name: String): Category = transaction {
        Category.new {
            this.name = name
        }
    }

    fun update(id: Int, name: String): Category? = transaction {
        Category.findById(id)?.apply {
            this.name = name
        }
    }

    fun delete(id: Int): Boolean = transaction {
        Category.findById(id)?.delete() != null
    }
}
