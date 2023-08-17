package kr.kro.refbook.services

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

    fun generateRefreshToken(oldRefreshToken: String? = null): Pair<String, Long> {
        oldRefreshToken?.let { deleteRefreshToken(it) }

        val refreshToken = UUID.randomUUID().toString()
        saveRefreshToken(refreshToken)
        return Pair(refreshToken, refreshTokenExpiration.toLong())
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
