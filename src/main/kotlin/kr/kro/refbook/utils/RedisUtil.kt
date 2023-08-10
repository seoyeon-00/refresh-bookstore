package kr.kro.refbook.utils

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisUtil(private val redisTemplate: StringRedisTemplate) {

    fun getData(key: String): String? {
        val valueOperations: ValueOperations<String, String> = redisTemplate.opsForValue()
        return valueOperations.get(key)
    }

    fun setDataExpire(key: String, value: String, duration: Long) {
        val valueOperations: ValueOperations<String, String> = redisTemplate.opsForValue()
        val expireDuration = Duration.ofMillis(duration)
        valueOperations.set(key, value, expireDuration)
    }

    fun deleteData(key: String) {
        redisTemplate.delete(key)
    }
}