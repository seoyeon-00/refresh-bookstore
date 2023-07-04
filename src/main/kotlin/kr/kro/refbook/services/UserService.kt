package kr.kro.refbook.services

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll()
    }

    fun getUserById(id:Int): List<UserDto> {
        return listOf(userRepository.findById(id))
    }

    fun createUser(userDto: UserDto): UserDto {
        return userRepository.create(userDto)
    }

    fun updateUser(id:Int, userDto: UserDto): UserDto? {
        return userRepository.update(id, userDto)
    }

    fun deleteUser(id:Int): Boolean {
        return userRepository.delete(id) > 0
    }

}