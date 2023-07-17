// package kr.kro.refbook.entities.tables

// import org.jetbrains.exposed.dao.id.IntIdTable
// import org.jetbrains.exposed.sql.Column
// import kr.kro.refbook.entities.tables.Users

// public enum class ROLE {
//     MEMBER
// }

// object MemberRoles : IntIdTable() {
//     val role: Column<ROLE> = enumerationByName("role", 20, ROLE::class)
//     val member: Column<EntityID<Int>> = reference("member", Users.id)
// }