package kr.kro.refbook.dto

import kr.kro.refbook.common.status.ROLE

data class MemberRoleDto(
    val id: Int,
    val role: ROLE
)