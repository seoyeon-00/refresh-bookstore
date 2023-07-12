package kr.kro.refbook.dto

import java.time.LocalDateTime
import kr.kro.refbook.entities.Users
import org.jetbrains.exposed.sql.ResultRow

data class UserDto(
  val id: Int,
  val name: String,
  val email: String,
  val password: String,
  val postalCode: String,
  val address: String,
  val detailAddress: String,
  val phone: String,
  val isAdmin: Boolean?,
  val createdAt: LocalDateTime?
) {
    companion object {
        fun fromRow(row: ResultRow): UserDto {
            return UserDto(
                id = row[Users.id].value,
                name = row[Users.name],
                email = row[Users.email],
                password = row[Users.password],
                postalCode = row[Users.postalCode],
                address = row[Users.address],
                detailAddress = row[Users.detailAddress],
                phone = row[Users.phone],
                isAdmin = row[Users.isAdmin],
                createdAt = row[Users.createdAt]
            )
        }
    }
}