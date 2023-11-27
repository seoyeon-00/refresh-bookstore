package kr.kro.refbook.dto
data class RefreshTokenRequest(
    val refreshToken: String,
    val email: String,
    val password: String,
)
