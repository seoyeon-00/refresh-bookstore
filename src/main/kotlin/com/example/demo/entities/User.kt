package com.example.demo.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object Users : IntIdTable() {
  val name: Column<String> = varchar("name", 50)
  val email: Column<String> = varchar("email", 50)
  val password: Column<String> = varchar("password", 100)
  val postalcode: Column<String> = varchar("postalcode", 10)
  val address: Column<String> = varchar("address", 100)
  val detailaddress: Column<String> = varchar("detailaddress", 100)
  val phone: Column<String> = varchar("phone", 20)
  val isadmin: Column<Boolean> = bool("isadmin").default(false)
  val createdat: Column<LocalDateTime> = datetime("createdat").default(LocalDateTime.now())
}