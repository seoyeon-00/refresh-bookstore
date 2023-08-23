package kr.kro.refbook.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.net.URI

@Configuration
public class RedisConfig {

    @Value("\${spring.data.redis.url}")
    private lateinit var redisUrl: String

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val uri = URI(redisUrl)
        val redisStandaloneConfiguration = RedisStandaloneConfiguration()

        redisStandaloneConfiguration.hostName = uri.host
        redisStandaloneConfiguration.port = uri.port

        // 패스워드가 있을 경우 적용
        if (uri.userInfo != null) {
            val password = uri.userInfo.split(":").last()
            redisStandaloneConfiguration.password = RedisPassword.of(password)
        }

        return LettuceConnectionFactory(redisStandaloneConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        redisTemplate.keySerializer = StringRedisSerializer()
        return redisTemplate
    }
}
