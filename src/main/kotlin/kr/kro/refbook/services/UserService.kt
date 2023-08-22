package kr.kro.refbook.services

import kr.kro.refbook.common.authority.JwtTokenProvider
import kr.kro.refbook.common.authority.TokenInfo
import kr.kro.refbook.common.exception.InvalidInputException
import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.dto.*
import kr.kro.refbook.entities.models.MemberRole
import kr.kro.refbook.entities.models.User
import kr.kro.refbook.repositories.MemberRoleRepository
import kr.kro.refbook.repositories.UserRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

import kr.kro.refbook.utils.MailServiceUtils
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils
import org.springframework.mail.javamail.MimeMessageHelper
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

@Service
class UserService(
    private val userRepository: UserRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val javaMailSender: JavaMailSender,
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

    fun updateTemporaryPasswordUser(passwordFindDto: PasswordFindDto): User {
        val user = userRepository.findByEmail(passwordFindDto.email)
        ?: throw IllegalArgumentException("User not found")

        if (user.birth == passwordFindDto.birth) {
            val temporaryPassword: String = MailServiceUtils.generateTemporaryPassword()
            val message = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            helper.setTo(passwordFindDto.email)
            helper.setSubject("[Refresh Bookstore] 임시 비밀번호가 발급되었습니다")
            helper.setText(getHtmlText(temporaryPassword), true)
            javaMailSender.send(message)
            
            return userRepository.updatePassword(user.id.value, temporaryPassword)
        }

        throw IllegalArgumentException("User birth does not match")
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
        birth = user.birth,
        isAdmin = user.isAdmin,
        createdAt = user.createdAt,
    )

    private fun toRoleDto(memberRole: MemberRole): MemberRoleDto = MemberRoleDto(
        id = memberRole.id.value,
        role = memberRole.role,
        member = memberRole.member.id.value,
    )

    private fun getHtmlText(temporaryPassword: String): String {
        val resource = ClassPathResource("email-FindPassword.html")
        val htmlContent = FileCopyUtils.copyToString(
            InputStreamReader(resource.inputStream, StandardCharsets.UTF_8)
        ).replace("\${temporaryPassword}", temporaryPassword)

        return htmlContent
    }
}
