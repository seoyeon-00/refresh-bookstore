package com.example.demo.repositories

import com.example.demo.dto.UserDto
import com.example.demo.entities.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class UserRepository{

    fun findAll(): List<UserDto> {
        return transaction {
            Users.selectAll().map { UserDto.fromRow(it) }
        }
    }
}