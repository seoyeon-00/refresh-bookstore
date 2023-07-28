package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.tables.OrderLists
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class OrderList(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<OrderList>(OrderLists)
    var product by Product referencedOn OrderLists.product
    var amount by OrderLists.amount
}