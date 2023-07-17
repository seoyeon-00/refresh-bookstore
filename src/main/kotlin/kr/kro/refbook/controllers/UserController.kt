package kr.kro.refbook.controllers

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.dto.LoginDto
import kr.kro.refbook.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import kr.kro.refbook.common.authority.TokenInfo
import kr.kro.refbook.common.dto.BaseResponse
import kr.kro.refbook.dto.UserDtoResponse
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

  @PostMapping("/signup")
  fun signUp(@RequestBody @Valid userDto: UserDto): UserDto {
    return userService.signUp(userDto)
  }

  @PostMapping("/login")
  fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
    val tokenInfo = userService.login(loginDto)
    return BaseResponse(data = tokenInfo)
  }

  @GetMapping("/info/{id}")
    fun searchMyInfo(@PathVariable id: Int): BaseResponse<UserDtoResponse> {
        //val userId = (SecurityContextHolder.getContext().authentication.principal as UserDto).id
        val response = userService.searchUser(id)
        return BaseResponse(data = response)
    }

}