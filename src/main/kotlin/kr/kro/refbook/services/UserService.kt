package kr.kro.refbook.services

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.dto.LoginDto
import kr.kro.refbook.dto.MemberRoleDto
import kr.kro.refbook.dto.UserDtoResponse
import kr.kro.refbook.entities.User
import kr.kro.refbook.entities.MemberRole
import kr.kro.refbook.repositories.UserRepository
import kr.kro.refbook.repositories.MemberRoleRepository
import org.springframework.stereotype.Service
import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.common.exception.InvalidInputException
import kr.kro.refbook.common.authority.JwtTokenProvider
import kr.kro.refbook.common.authority.TokenInfo
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.jetbrains.exposed.sql.transactions.transaction

@Service
class UserService(
    private val userRepository: UserRepository, 
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
) {

    //회원가입
    fun signUp(userDto: UserDto): UserDto {

        //Email 중복 검사
        val existingUser = userRepository.findByEmail(userDto.email)
        if (existingUser != null) {
            throw InvalidInputException("email","이미 등록된 이메일입니다.")
        }

        val newUser = userRepository.create(userDto)
        memberRoleRepository.createMemberRole(newUser, ROLE.MEMBER)
        return toDto(newUser)
    }

    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    fun searchUser(id: Int): UserDtoResponse {
        val user: User = userRepository.findById(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return user.toDtoResponse()
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
        password = passwordEncoder.encode(user.password),
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
