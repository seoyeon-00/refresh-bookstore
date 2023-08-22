package kr.kro.refbook.entities.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
    val email: Column<String> = varchar("email", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 200)
    val postalCode: Column<String> = varchar("postalCode", 10)
    val address: Column<String> = varchar("address", 100)
    val detailAddress: Column<String> = varchar("detailAddress", 100)
    val phone: Column<String> = varchar("phone", 20)
    val birth: Column<String> = varchar("birth", 20)
    val isAdmin: Column<Boolean> = bool("isAdmin").default(false)
    val createdAt: Column<LocalDateTime> = datetime("createdAt").default(LocalDateTime.now())
}