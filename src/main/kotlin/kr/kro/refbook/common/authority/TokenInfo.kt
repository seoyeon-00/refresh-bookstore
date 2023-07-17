package kr.kro.refbook.common.authority

data class TokenInfo (
  val grantType: String,
  val accessToken: String,
)