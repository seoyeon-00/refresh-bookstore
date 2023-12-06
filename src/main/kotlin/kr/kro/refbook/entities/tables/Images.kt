package kr.kro.refbook.entities.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object Images : IntIdTable() {
    val fileName: Column<String> = varchar("file_name", 255)
    val fileUrl: Column<String> = varchar("file_url", 255)
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
}

