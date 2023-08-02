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
                it.requestMatchers("/api/user/signup", "/api/user/login", "/api/user/admin/role/{id}").anonymous()
                    .requestMatchers("/api/user", "/api/user/info").hasAnyRole("MEMBER", "ADMIN")
                    .requestMatchers("/api/user/admin/{id}", "/api/user/admin").hasRole("ADMIN")
                    .requestMatchers("/api/products/isbn/{isbn}").hasAnyRole("MEMBER", "ADMIN") // 상품 조회, 검색, ISBN으로 조회
                    .requestMatchers("/api/products").permitAll() // 모두가 할 수 있는 상품 조회
                    .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN") // 상품 생성
                    .requestMatchers(HttpMethod.PUT, "/api/products/isbn/{isbn}").hasRole("ADMIN") // 상품 수정
                    .requestMatchers(HttpMethod.DELETE, "/api/products/isbn/{isbn}").hasRole("ADMIN") // 상품 삭제
                    .requestMatchers("/api/orders/{id}", "/api/orders/orderNumber/{orderNumber}").authenticated() // 주문 조회, 생성, 삭제, 수정 - 로그인한 사용자
                    .requestMatchers("/api/orders").hasRole("ADMIN") // 주문 전체조회 - 관리자 조회
                    .requestMatchers(HttpMethod.POST, "/api/orders").authenticated() // 주문 생성 - 로그인한 사용자
                    .requestMatchers("/api/categories/{id}").authenticated() // 카테고리 조회 - 모두
                    .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN") // 카테고리 생성 - 관리자
                    .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasRole("ADMIN") // 카테고리 삭제 - 관리자
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
