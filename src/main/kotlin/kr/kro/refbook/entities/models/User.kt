// package kr.kro.refbook.entities.models

// import kr.kro.refbook.entities.tables.Users
// import org.jetbrains.exposed.dao.IntEntity
// import org.jetbrains.exposed.dao.IntEntityClass
// import org.jetbrains.exposed.dao.id.EntityID

// class User(id: EntityID<Int>) : IntEntity(id) {
//     companion object : IntEntityClass<User>(Users)
  
//     var name by Users.name
//     var email by Users.email
//     var password by Users.password
//     var postalCode by Users.postalCode
//     var address by Users.address
//     var detailAddress by Users.detailAddress
//     var phone by Users.phone
//     var isAdmin by Users.isAdmin
//     var createdAt by Users.createdAt
// }