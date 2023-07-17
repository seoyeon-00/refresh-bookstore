package kr.kro.refbook.common.service

import kr.kro.refbook.entities.User
import kr.kro.refbook.repositories.UserRepository
import kr.kro.refbook.common.dto.CustomUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
// import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)
            ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("해당 유저는 없습니다.")

    private fun createUserDetails(user: User): UserDetails {
        val memberRoles = user.fetchMemberRoles()
        val authorities = memberRoles.map { SimpleGrantedAuthority("ROLE_${it.role}") }
        return CustomUser(
            user.id.value.toInt(),
            user.email,
            passwordEncoder.encode(user.password),
            authorities 
        )
    }
}