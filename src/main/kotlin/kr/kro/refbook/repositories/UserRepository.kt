package kr.kro.refbook.repositories

import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.entities.User
import kr.kro.refbook.entities.Users
import kr.kro.refbook.entities.MemberRole
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import kr.kro.refbook.common.status.ROLE

@Repository
class UserRepository{
    fun findByEmail(email: String): User? = transaction {
        User.find { Users.email eq email }.singleOrNull()
    }

    fun create(userDto: UserDto): User = transaction {
        User.new {
            name = userDto.name
            email = userDto.email
            password = userDto.password
            postalCode = userDto.postalCode
            address = userDto.address
            detailAddress = userDto.detailAddress
            phone = userDto.phone
            isAdmin = userDto.isAdmin ?: false
            createdAt = userDto.createdAt ?: LocalDateTime.now()
        }.apply {
            memberRoles // 추가: memberRoles 참조 추가
        }
    }
}


@Repository
class MemberRoleRepository {
    fun create(memberRole: MemberRole): MemberRole = transaction {
        val savedMemberRole = MemberRole.new {
            role = memberRole.role
            member = memberRole.member
        }
        savedMemberRole
    }

    fun createMemberRole(user: User, role: ROLE): MemberRole = transaction {
        val memberRole = MemberRole.new {
            this.role = role
            member = user
        }
        memberRole
    }

}
