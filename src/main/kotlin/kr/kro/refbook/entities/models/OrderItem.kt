package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.tables.OrderItems
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderItem(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderItem>(OrderItems)

    var product by Product referencedOn OrderItems.product
    var amount by OrderItems.amount
    var order by Order optionalReferencedOn OrderItems.order
}