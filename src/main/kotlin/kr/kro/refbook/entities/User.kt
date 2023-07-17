package kr.kro.refbook.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
    val email: Column<String> = varchar("email", 50)
    val password: Column<String> = varchar("password", 100)
    val postalCode: Column<String> = varchar("postalCode", 10)
    val address: Column<String> = varchar("address", 100)
    val detailAddress: Column<String> = varchar("detailAddresss", 100)
    val phone: Column<String> = varchar("phone", 20)
    val isAdmin: Column<Boolean> = bool("isAdmin").default(false)
    val createdAt: Column<LocalDateTime> = datetime("createdAt").default(LocalDateTime.now())
}
