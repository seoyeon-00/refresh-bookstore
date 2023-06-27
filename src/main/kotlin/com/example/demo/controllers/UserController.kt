package com.example.demo.controllers

import com.example.demo.dto.UserDto
import com.example.demo.services.UserService
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