package kr.kro.refbook.common.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    val userId: Int,
    userName: String,
    password: String,
    authorities: Collection<GrantedAuthority>,
) : User(userName, password, authorities)
