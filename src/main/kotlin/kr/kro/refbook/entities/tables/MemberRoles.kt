package kr.kro.refbook.entities.tables

import kr.kro.refbook.common.status.ROLE
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object MemberRoles : IntIdTable() {
    val role: Column<ROLE> = enumerationByName("role", 20, ROLE::class)
    val member = reference("member", Users)
}

