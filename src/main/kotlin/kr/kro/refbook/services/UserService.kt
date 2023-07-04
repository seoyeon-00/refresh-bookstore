package kr.kro.refbook.services

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll()
    }
}