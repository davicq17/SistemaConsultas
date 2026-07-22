package co.edu.iub.sistemaconsultas.config

import co.edu.iub.sistemaconsultas.service.CustomUserDetailsService
import co.edu.iub.sistemaconsultas.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService
): OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = authHeader.substring(7)

            val userEmail = jwtService.extraerCorreo(jwt)

            val userDetails = userDetailsService.loadUserByUsername(userEmail)

            if(!jwtService.esTokenValido(jwt,userDetails)){
                filterChain.doFilter(request, response)
                return
            }

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            filterChain.doFilter(request, response)
            return
        }

        filterChain.doFilter(request, response)
    }
}