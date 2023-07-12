package kr.kro.refbook.controllers

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

//   @Autowired
//   lateinit var userService: UserService

//   @GetMapping
//     fun getAllUsers(): ResponseEntity<List<UserDto>> {
//         return ResponseEntity.ok(userService.getAllUsers())
//     }

//   @GetMapping("/{id}")
//     fun getUserById(@PathVariable id: Int): ResponseEntity<List<UserDto>> {
//         return ResponseEntity.ok(userService.getUserById(id))
//     }

//   @PostMapping
//     fun createUsers(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
//         val createdUser = userService.createUser(userDto)
//         return ResponseEntity.ok(createdUser)
//     }

//   @PutMapping("/{id}")
//     fun updateUser(@PathVariable id: Int, @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
//       val updatedUser = userService.updateUser(id, userDto)
//       return if (updatedUser != null) {
//         ResponseEntity.ok(updatedUser)
//       } else {
//         ResponseEntity.notFound().build()
//       }
//     }

//   @DeleteMapping("/{id}")
//     fun deleteUser(@PathVariable id: Int): ResponseEntity<Unit> {
//       return if (userService.deleteUser(id)) {
//           ResponseEntity.ok().build()
//       } else {
//           ResponseEntity.notFound().build()
//       }
//   }
}