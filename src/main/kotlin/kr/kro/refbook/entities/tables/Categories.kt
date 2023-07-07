package kr.kro.refbook.entities.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable() {
    val name = varchar("name", 255).uniqueIndex()
}
