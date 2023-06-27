package com.example.demo.services

import com.example.demo.dto.UserDto
import com.example.demo.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll()
    }
}