package kr.kro.refbook.common.authority

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val corsConfig: CorsConfig,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {
                it.configurationSource(corsConfig.corsConfigurationSource())
            }
            .httpBasic { it.disable() }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/user/signup", "/api/user/login", "/api/user/checkEmail", "/api/user/admin/role/{id}", "/api/products", "/api/user/refresh-token").permitAll()
                    .requestMatchers("/api/user", "/api/user/info", "/api/user/check","/api/products/isbn/{isbn}", "/api/orders/{id}", "/api/orders/orderNumber/{orderNumber}").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/products", "/api/categories/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/products/isbn/{isbn}", "/api/categories/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/isbn/{isbn}", "/api/categories/{id}").hasRole("ADMIN")
                    .requestMatchers("/api/user/admin/{id}", "/api/user/admin").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()
                    .anyRequest().permitAll()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
