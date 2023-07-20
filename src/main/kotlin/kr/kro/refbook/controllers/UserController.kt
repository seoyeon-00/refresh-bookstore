package kr.kro.refbook.controllers

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.dto.LoginDto
import kr.kro.refbook.dto.MemberRoleDto
import kr.kro.refbook.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import kr.kro.refbook.common.authority.TokenInfo
import kr.kro.refbook.common.dto.BaseResponse
import kr.kro.refbook.dto.UserDtoResponse
import org.springframework.security.core.context.SecurityContextHolder
import kr.kro.refbook.common.dto.CustomUser

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

    // @GetMapping("/{id}")
    // fun searchMyInfo(@PathVariable id: Int): BaseResponse<UserDtoResponse> {
    //     val response = userService.searchUser(id)
    //     return BaseResponse(data = response)
    // }

    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<UserDtoResponse> {
          val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
          val response = userService.searchUser(userId)
          return BaseResponse(data = response)
      }

    @GetMapping
    fun searchMyInfoAll(): BaseResponse<List<UserDtoResponse>> {
        val response = userService.searchUserAll()
        return BaseResponse(data = response)
    }

    @PutMapping
    fun updateUser(@RequestBody @Valid userDto: UserDto): BaseResponse<UserDto> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        userDto.id = userId
        val response = userService.updateUser(userDto) 
        return BaseResponse(data = response, message = "User updated successfully")
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
}
