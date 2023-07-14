package kr.kro.refbook.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import java.time.LocalDateTime
import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.entities.Users
import kr.kro.refbook.entities.User


object MemberRoles : IntIdTable() {
    val role: Column<ROLE> = enumerationByName("role", 20, ROLE::class)
    val member = reference("member", Users)
}

class MemberRole(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MemberRole>(MemberRoles)

    var role by MemberRoles.role
    var member by User referencedOn MemberRoles.member
    
}
