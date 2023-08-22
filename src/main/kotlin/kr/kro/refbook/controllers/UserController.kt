package kr.kro.refbook.controllers

import jakarta.validation.Valid
import kr.kro.refbook.common.authority.JwtTokenProvider
import kr.kro.refbook.common.authority.TokenInfo
import kr.kro.refbook.common.dto.BaseResponse
import kr.kro.refbook.common.dto.CustomUser
import kr.kro.refbook.utils.MailServiceUtils
import kr.kro.refbook.dto.*
import kr.kro.refbook.repositories.UserRepository
import kr.kro.refbook.services.RefreshTokenService
import kr.kro.refbook.services.UserService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val bcryptPasswordEncoder: BCryptPasswordEncoder,
    private val refreshTokenService: RefreshTokenService,
    private val jwtTokenProvider: JwtTokenProvider,

    ) {
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid userDto: UserDto): UserDto {
        return userService.signUp(userDto)
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): ResponseEntity<Map<String, Any>> {
        val user = userRepository.findByEmail(loginDto.email)
        if (user != null) {
            val hashedPassword = user.password
            if (bcryptPasswordEncoder.matches(loginDto.password, hashedPassword)) {
                val loginDtoWithHashedPassword = loginDto.copy(password = hashedPassword)
                val tokenInfo = userService.login(loginDtoWithHashedPassword)

                return ResponseEntity.ok(tokenInfo)
            }
        }
        throw BadCredentialsException("Invalid email or password")
    }

    // 유저 - 패스워드 일치 여부 확인하기
    @PostMapping("/check")
    fun checkPassword(@RequestBody @Valid passwordAuthenticationDto: PasswordAuthenticationDto): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = userService.searchUserPassword(userId)
        val hashedPassword = response.password
        if (bcryptPasswordEncoder.matches(passwordAuthenticationDto.password, hashedPassword)) {
            return BaseResponse(message = "비밀번호 인증이 완료되었습니다.")
        }
        throw BadCredentialsException("비밀번호 인증을 실패하였습니다.")
    }

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody @Valid checkEmailRequestDto: CheckEmailRequestDto): BaseResponse<Unit> {
        val response = userService.checkEmail(checkEmailRequestDto)
        val message = if (response) {
            "Available email"
        } else {
            "User already exists"
        }
        return BaseResponse(message = message)
    }

    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<UserDtoResponse> {
        val userEmail = (SecurityContextHolder.getContext().authentication.principal as CustomUser).username
        val response = userService.searchUser(userEmail)
        return BaseResponse(data = response)
    }

    @GetMapping("/admin")
    fun searchMyInfoAll(): BaseResponse<List<UserDtoResponse>> {
        val response = userService.searchUserAll()
        return BaseResponse(data = response)
    }

    @PutMapping
    fun updateUser(@RequestBody userDto: UserDto): BaseResponse<UserDto> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        userDto.id = userId
        val response = userService.updateUser(userDto)
        return BaseResponse(data = response, message = "User updated successfully")
    }

    // 임시 비밀번호 발급하기
    @PutMapping("/temporary")
    fun updateUser(@RequestBody passwordFindDto: PasswordFindDto): BaseResponse<Unit> {
        userService.updateTemporaryPasswordUser(passwordFindDto)
        return BaseResponse(message = "Temporary password updated successfully")
    }

    @PutMapping("/admin/role/{id}")
    fun updateUserRole(@PathVariable id: Int, @RequestBody memberRoleDto: MemberRoleDto): BaseResponse<MemberRoleDto> {
        val response = userService.updateUserRole(id, memberRoleDto)
        return BaseResponse(data = response, message = "Role updated successfully")
    }

    @DeleteMapping("/admin/{id}")
    fun deleteUser(@PathVariable id: Int): BaseResponse<Unit> {
        val response = userService.deleteUser(id)
        val message = if (response) {
            "User deleted successfully"
        } else {
            "User not found"
        }
        return BaseResponse(message = message)
    }

    @DeleteMapping
    fun deleteUser(): BaseResponse<Unit> {
        val id = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = userService.deleteUser(id)
        val message = if (response) {
            "User deleted successfully"
        } else {
            "User not found"
        }
        return BaseResponse(message = message)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<Map<String, Any>> {

        if (refreshTokenService.validateRefreshToken(refreshTokenRequest.refreshToken)) {
            val authentication = SecurityContextHolder.getContext().authentication
            val newTokenInfo = jwtTokenProvider.createToken(authentication)

            // 새로운 리프레시 토큰 생성하며 기존 토큰 삭제
            val (newRefreshToken, expiration) = refreshTokenService.generateRefreshToken(refreshTokenRequest.refreshToken)

            val responseBody = mapOf(
                "grantType" to newTokenInfo.grantType,
                "accessToken" to newTokenInfo.accessToken,
                "refreshToken" to newRefreshToken,
                "refreshTokenExpiration" to expiration  // expiration 정보 추가
            )
            return ResponseEntity.ok(responseBody)
        } else {
            throw BadCredentialsException("Invalid or expired refresh token")
        }
    }

}
