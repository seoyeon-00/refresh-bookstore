package kr.kro.refbook.common.authority

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : GenericFilterBean() {

    // override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
    //     val token = resolveToken(request as HttpServletRequest)

    //     if (token != null && jwtTokenProvider.validateToken(token)) {
    //         val authentication = jwtTokenProvider.getAuthentication(token)
    //         SecurityContextHolder.getContext().authentication = authentication
    //     }

    //     chain?.doFilter(request, response)
    // }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token = resolveToken(request as HttpServletRequest)

        if (token != null) {
            try {
                // 토큰이 유효하면 Authentication 객체를 설정
                if (jwtTokenProvider.validateToken(token)) {
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: Exception) {
                // 토큰이 유효하지 않아도 그냥 통과
                // 여기서 예외를 무시하도록 수정
            }
        }

        chain?.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")

        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
