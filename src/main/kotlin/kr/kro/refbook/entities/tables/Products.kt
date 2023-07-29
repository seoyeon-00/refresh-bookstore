package kr.kro.refbook.entities.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

object Products : IntIdTable() {
    val category: Column<EntityID<Int>> = reference("category_id", Categories)
    val title: Column<String> = varchar("title", 50)
    val author: Column<String> = varchar("author", 50)
    val publisher: Column<String> = varchar("publisher", 100)
    val publicationDate: Column<LocalDate> = date("publication_date")
    val isbn: Column<String> = varchar("isbn", 100).uniqueIndex()
    val description: Column<String> = text("description")
    val price: Column<BigDecimal> = decimal("price", 10, 2)
    val imagePath: Column<String> = varchar("image_path", 100)
    val isBestSeller: Column<Boolean> = bool("is_best_seller").default(false)
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
}
