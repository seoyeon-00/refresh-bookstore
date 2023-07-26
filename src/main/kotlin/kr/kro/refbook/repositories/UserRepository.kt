package kr.kro.refbook.repositories

import kr.kro.refbook.common.status.ROLE
import kr.kro.refbook.dto.MemberRoleDto
import kr.kro.refbook.dto.UserDto
import kr.kro.refbook.entities.MemberRole
import kr.kro.refbook.entities.MemberRoles
import kr.kro.refbook.entities.User
import kr.kro.refbook.entities.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserRepository(
    private val bcryptPasswordEncoder: BCryptPasswordEncoder,
) {

    init {
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(MemberRoles)
        }
    }

    fun findByEmail(email: String): User? = transaction {
        User.find { Users.email eq email }.singleOrNull()
    }

    fun create(userDto: UserDto): User = transaction {
        User.new {
            name = userDto.name
            email = userDto.email
            password = bcryptPasswordEncoder.encode(userDto.password)
            postalCode = userDto.postalCode
            address = userDto.address
            detailAddress = userDto.detailAddress
            phone = userDto.phone
            isAdmin = userDto.isAdmin ?: false
            createdAt = userDto.createdAt ?: LocalDateTime.now()
        }.apply {
            memberRoles
        }
    }

    fun findById(id: Int): User? {
        return transaction {
            User.findById(id)
        }
    }

    fun findAll(): List<User> {
        return transaction {
            User.all().toList()
        }
    }

    fun update(userDto: UserDto): User = transaction {
        val user = User.findById(userDto.id) ?: error("User not found")

        if (userDto.email != user.email) {
            throw IllegalArgumentException("Email cannot be changed")
        }

        if (userDto.password != user.password) {
            user.password = bcryptPasswordEncoder.encode(userDto.password)
        }

        user.apply {
            name = userDto.name
            // email = userDto.email
            postalCode = userDto.postalCode
            address = userDto.address
            detailAddress = userDto.detailAddress
            phone = userDto.phone
            // isAdmin = userDto.isAdmin ?: false
        }
        user
    }

    fun delete(id: Int): Boolean = transaction {
        val user = User.findById(id)
        user?.let {
            user.memberRoles.forEach { memberRole ->
                memberRole.delete()
            }
            user.delete()
            true
        } ?: false
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

    fun update(memberId: Int, memberRoleDto: MemberRoleDto): MemberRole? = transaction {
        val member = User.findById(memberId)
        val memberRole = member?.let {
            MemberRole.find { MemberRoles.member eq it.id }.singleOrNull()
        }
        memberRole?.apply {
            role = memberRoleDto.role
            member.isAdmin = role == ROLE.ADMIN
        }
        memberRole
    }
}
