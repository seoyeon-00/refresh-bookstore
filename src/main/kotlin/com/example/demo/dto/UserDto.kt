package com.example.demo.dto

import java.time.LocalDateTime
import com.example.demo.entities.Users
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
  val id: Int, 
  val name: String,
  val email: String,
  val password: String,
  val postalcode: String,
  val address: String,
  val detailaddress: String,
  val phone: String,
  val isadmin: Boolean,
  val createdat: LocalDateTime
) {
    companion object {
        fun fromRow(row: ResultRow): UserDto {
            return UserDto(
                id = row[Users.id].value,
                name = row[Users.name],
                email = row[Users.email],
                password = row[Users.password],
                postalcode = row[Users.postalcode],
                address = row[Users.address],
                detailaddress = row[Users.detailaddress],
                phone = row[Users.phone],
                isadmin = row[Users.isadmin],
                createdat = row[Users.createdat]
            )
        }
    }
}