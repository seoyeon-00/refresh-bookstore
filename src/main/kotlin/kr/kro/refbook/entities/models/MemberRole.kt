package kr.kro.refbook.entities.models

import kr.kro.refbook.entities.tables.MemberRoles
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class MemberRole(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MemberRole>(MemberRoles)

    var role by MemberRoles.role
    var member by User referencedOn MemberRoles.member
}