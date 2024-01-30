package kr.kro.refbook.services

import kr.kro.refbook.common.dto.CustomUser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*
import java.util.concurrent.TimeUnit


@Service
class RefreshTokenService {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Value("\${refresh.token.expiration}")
    private lateinit var refreshTokenExpiration: String

     @Value("\${jwt.secret}")
    lateinit var secretKey: String

    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun generateRefreshToken(authentication: Authentication, oldRefreshToken: String? = null): Pair<String, Long> {
        oldRefreshToken?.let { deleteRefreshToken(it) }

        val refreshToken = createJwtToken(authentication)
        saveRefreshToken(refreshToken)
        
        return Pair(refreshToken, refreshTokenExpiration.toLong())
    }

    private fun createJwtToken(authentication: Authentication): String {
        val now = Date()
        val expiration = Date(now.time + refreshTokenExpiration.toLong() * 1000) // seconds to milliseconds
        // val authorities: String = authentication
        //     .authorities
        //     .joinToString(",", transform = GrantedAuthority::getAuthority)

        return Jwts.builder()
            .setSubject("refreshToken")
            .setIssuedAt(now)
            // .claim("auth", authorities)
            .claim("userId", (authentication.principal as CustomUser).userId)
            // .claim("username", (authentication.principal as CustomUser).username)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS256)
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
