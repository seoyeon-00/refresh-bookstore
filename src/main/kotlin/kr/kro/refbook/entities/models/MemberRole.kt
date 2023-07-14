// package kr.kro.refbook.entities.models

// import kr.kro.refbook.entities.tables.MemberRoles
// import kr.kro.refbook.entities.tables.MemberRoles.ROLE
// import kr.kro.refbook.entities.models.User
// import org.jetbrains.exposed.dao.IntEntity
// import org.jetbrains.exposed.dao.IntEntityClass
// import org.jetbrains.exposed.dao.id.EntityID


// class MemberRole(id: EntityID<Int>) : IntEntity(id) {
//     companion object : IntEntityClass<MemberRole>(MemberRoles)

//     var role by MemberRoles.role
//     var member by User referencedOn MemberRoles.member

//     constructor(role: ROLE, member: User) : this(EntityID(0, MemberRoles)) {
//         this.role = role
//         this.member = member
//     }
// }
