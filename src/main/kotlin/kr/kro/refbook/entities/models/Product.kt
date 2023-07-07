package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.tables.Products
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Product(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Product>(Products)

    var category by Category referencedOn Products.category
    var title by Products.title
    var author by Products.author
    var publisher by Products.publisher
    var publicationDate by Products.publicationDate
    var isbn by Products.isbn
    var description by Products.description
    var price by Products.price
    var imagePath by Products.imagePath
    var isBestSeller by Products.isBestSeller
    var createdAt by Products.createdAt
}
