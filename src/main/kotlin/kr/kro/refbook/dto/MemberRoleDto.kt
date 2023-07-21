package kr.kro.refbook.dto

import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.entities.User
import org.jetbrains.exposed.dao.id.EntityID

data class MemberRoleDto(
    val id: Int,
    val role: ROLE,
    //val member: EntityID<Int>?
    val member: Int
)