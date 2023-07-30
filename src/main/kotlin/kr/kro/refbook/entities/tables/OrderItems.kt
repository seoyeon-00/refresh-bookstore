package kr.kro.refbook.entities.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object OrderItems : IntIdTable() {
    val product: Column<EntityID<Int>> = reference("product_id", Products)
    val amount: Column<Int> = integer("amount")
    val order: Column<EntityID<Int>?> = reference("order_id", Orders).nullable()
}
