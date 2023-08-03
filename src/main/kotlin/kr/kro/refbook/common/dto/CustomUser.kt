package kr.kro.refbook.common.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val userId: Int,
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority>,
) : User(username, password, authorities)
