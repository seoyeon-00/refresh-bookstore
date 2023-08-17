package kr.kro.refbook.services

import kr.kro.refbook.common.authority.JwtTokenProvider
import kr.kro.refbook.common.authority.TokenInfo
import kr.kro.refbook.common.exception.InvalidInputException
import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.dto.CheckEmailRequestDto
import kr.kro.refbook.dto.LoginDto
import kr.kro.refbook.dto.MemberRoleDto
import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.dto.UserDtoResponse
import kr.kro.refbook.entities.models.MemberRole
import kr.kro.refbook.entities.models.User
import kr.kro.refbook.repositories.MemberRoleRepository
import kr.kro.refbook.repositories.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    // 회원가입
    fun signUp(userDto: UserDto): UserDto {
        // Email 중복 검사
        val existingUser = userRepository.findByEmail(userDto.email)
        if (existingUser != null) {
            throw InvalidInputException("email", "Email already registered")
        }

        val newUser = userRepository.create(userDto)
        memberRoleRepository.createMemberRole(newUser, ROLE.MEMBER)
        return toDto(newUser)
    }

    fun login(loginDto: LoginDto): Map<String, Any> {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val (accessTokenInfo, refreshTokenPair) = jwtTokenProvider.createTokens(authentication)
        val (refreshToken, expiration) = refreshTokenPair

        return mapOf(
            "grantType" to accessTokenInfo.grantType,
            "accessToken" to accessTokenInfo.accessToken,
            "refreshToken" to refreshToken,
            "refreshTokenExpiration" to expiration
        )
    }


    fun searchUserPassword(id: Int): UserDtoResponse {
        val user: User = userRepository.findById(id) ?: throw InvalidInputException("id", "Not found User with Id($id)")
        return user.toDtoResponse()
    }

    fun searchUser(userEmail: String): UserDtoResponse {
        val user: User = userRepository.findByEmail(userEmail) ?: throw InvalidInputException("email", "Not found User with Id($userEmail)")
        return user.toDtoResponse()
    }

    fun checkEmail(checkEmailRequestDto: CheckEmailRequestDto): Boolean {
        val existingUser = userRepository.findByEmail(checkEmailRequestDto.email)
        if (existingUser != null) {
            throw InvalidInputException("email", "Email already registered")
        }
        return true
    }

    fun searchUserAll(): List<UserDtoResponse> {
        val userList: List<User> = userRepository.findAll()
        return userList.map { it.toDtoResponse() }
    }

    fun updateUserRole(memberId: Int, memberRoleDto: MemberRoleDto): MemberRoleDto = transaction {
        val updatedMemberRole = memberRoleRepository.update(memberId, memberRoleDto)
        toRoleDto(updatedMemberRole ?: throw IllegalArgumentException("No member role with id $memberId found."))
    }

    fun updateUser(userDto: UserDto): UserDto {
        val user = userRepository.update(userDto)
        return toDto(user)
    }

    fun deleteUser(id: Int): Boolean {
        val userToDelete = userRepository.findById(id)
            ?: throw IllegalArgumentException("No product with id $id found.")

        return userRepository.delete(userToDelete.id.value)
    }

    private fun toDto(user: User): UserDto = UserDto(
        id = user.id.value,
        name = user.name,
        email = user.email,
        password = user.password,
        postalCode = user.postalCode,
        address = user.address,
        detailAddress = user.detailAddress,
        phone = user.phone,
        isAdmin = user.isAdmin,
        createdAt = user.createdAt,
    )

    private fun toRoleDto(memberRole: MemberRole): MemberRoleDto = MemberRoleDto(
        id = memberRole.id.value,
        role = memberRole.role,
        member = memberRole.member.id.value,
    )
}
