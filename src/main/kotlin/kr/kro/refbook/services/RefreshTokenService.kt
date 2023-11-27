package kr.kro.refbook.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class RefreshTokenService {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Value("\${refresh.token.expiration}")
    private lateinit var refreshTokenExpiration: String

    private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateRefreshToken(oldRefreshToken: String? = null): Pair<String, Long> {
        oldRefreshToken?.let { deleteRefreshToken(it) }

        //val refreshToken = UUID.randomUUID().toString()
        val refreshToken = createJwtToken()
        saveRefreshToken(refreshToken)
        return Pair(refreshToken, refreshTokenExpiration.toLong())
    }

    private fun createJwtToken(): String {
        val now = Date()
        val expiration = Date(now.time + refreshTokenExpiration.toLong() * 1000) // seconds to milliseconds

        return Jwts.builder()
            .setSubject("refreshToken")
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

    private fun saveRefreshToken(refreshToken: String) {
        val ops: ValueOperations<String, String> = redisTemplate.opsForValue()
        ops.set(refreshToken, refreshToken, refreshTokenExpiration.toLong(), TimeUnit.SECONDS)
    }

    fun validateRefreshToken(refreshToken: String): Boolean {
        return redisTemplate.hasKey(refreshToken)
    }

    fun deleteRefreshToken(refreshToken: String) {
        redisTemplate.delete(refreshToken)
    }
}
