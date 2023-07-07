// package kr.kro.refbook.repositories

// import kr.kro.refbook.dto.UserDto
// import kr.kro.refbook.entities.Users
// import org.jetbrains.exposed.sql.selectAll
// import org.jetbrains.exposed.sql.transactions.transaction
// import org.jetbrains.exposed.sql.*
// import org.springframework.stereotype.Repository
// import java.time.LocalDateTime

// @Repository
// class UserRepository{

//     fun findAll(): List<UserDto> {
//         return transaction {
//             Users.selectAll().map { UserDto.fromRow(it) }
//         }
//     }

//     fun findById(id: Int): UserDto {
//         return transaction {
//             Users.select { Users.id eq id }.mapNotNull { UserDto.fromRow(it) }.singleOrNull()
//         } ?: throw NoSuchElementException("User not found")
//     }

//     fun create(userDto: UserDto): UserDto {
//         return transaction {
//             val id = Users.insertAndGetId {
//                 it[name] = userDto.name
//                 it[email] = userDto.email
//                 it[password] = userDto.password
//                 it[postalcode] = userDto.postalcode
//                 it[address] = userDto.address
//                 it[detailaddress] = userDto.detailaddress
//                 it[phone] = userDto.phone
//                 // it[isadmin] = userDto.isadmin
//                 // it[createdat] = LocalDateTime.now()
//             }
//             userDto.copy(id = id.value)
//         }
//     }

//     fun update(id:Int, userDto: UserDto): UserDto? {
//         return transaction {
//             val updatedRowCount = Users.update({ Users.id eq id }) {
//                 it[name] = userDto.name
//                 it[email] = userDto.email
//                 it[password] = userDto.password
//                 it[postalcode] = userDto.postalcode
//                 it[address] = userDto.address
//                 it[detailaddress] = userDto.detailaddress
//                 it[phone] = userDto.phone
//             }
//             if (updatedRowCount > 0) {
//                 UserDto.fromRow(Users.select { Users.id eq id }.singleOrNull() ?: throw RuntimeException("User not found"))
//             } else {
//                 throw RuntimeException("Update failed")
//             }
//         }
//     }

//     fun delete(id: Int): Int {
//         return transaction {
//             Users.deleteWhere { Users.id eq id }
//         }
//     }
// }