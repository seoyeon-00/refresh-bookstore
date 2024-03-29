package kr.kro.refbook.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.core.Ordered 
import org.springframework.core.annotation.Order  
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsConfig {
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()

        config.allowCredentials = true
        config.allowedOrigins = listOf("http://localhost:3000", "https://refresh-bookstore-frontend-qggshl9a0-seoyeon-00.vercel.app/","https://refresh-bookstore-frontend-git-feature-ksy-seoyeon-00.vercel.app/")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        config.allowedHeaders = listOf("Origin", "X-Requested-With", "Content-Type", "Authorization", "Oauth-Token")
        // config.exposedHeaders = listOf("*")
        config.exposedHeaders = listOf("Authorization", "Oauth-Token")

        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
