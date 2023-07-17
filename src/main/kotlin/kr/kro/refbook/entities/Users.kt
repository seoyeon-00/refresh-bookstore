package kr.kro.refbook.entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
// import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import java.time.LocalDateTime
import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.entities.MemberRoles
import org.jetbrains.exposed.sql.transactions.transaction

object Users : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
    val email: Column<String> = varchar("email", 50).uniqueIndex()
    val password: Column<String> = varchar("password", 100)
    val postalCode: Column<String> = varchar("postalCode", 10)
    val address: Column<String> = varchar("address", 100)
    val detailAddress: Column<String> = varchar("detailAddress", 100)
    val phone: Column<String> = varchar("phone", 20)
    val isAdmin: Column<Boolean> = bool("isAdmin").default(false)
    val createdAt: Column<LocalDateTime> = datetime("createdAt").default(LocalDateTime.now())

}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    
    var name by Users.name
    var email by Users.email
    var password by Users.password
    var postalCode by Users.postalCode
    var address by Users.address
    var detailAddress by Users.detailAddress
    var phone by Users.phone
    var isAdmin by Users.isAdmin
    var createdAt by Users.createdAt

    val memberRoles by MemberRole referrersOn MemberRoles.member

    fun fetchMemberRoles(): List<MemberRole> = transaction {
        MemberRole.find { MemberRoles.member eq this@User.id }
            .toList()
    }
}