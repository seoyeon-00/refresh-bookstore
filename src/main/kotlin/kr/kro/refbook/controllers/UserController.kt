package kr.kro.refbook.controllers

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping
class UserController {

  @Autowired
  lateinit var userService: UserService

  @GetMapping("/users")
  fun getAllUsers() = userService.getAllUsers()
}