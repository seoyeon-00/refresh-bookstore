package kr.kro.refbook.services

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.dto.LoginDto
import kr.kro.refbook.dto.MemberRoleDto
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

@Service
class UserService(
    private val userRepository: UserRepository, 
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
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
}
