package com.example.demo.entities

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.Table

object Categories : IntIdTable() {
    val name = varchar("name", 255).uniqueIndex()
    val categoryId = integer("categoryId").uniqueIndex()
}

class Category(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Category>(Categories)

    var name by Categories.name
    var categoryId by Categories.categoryId
}
