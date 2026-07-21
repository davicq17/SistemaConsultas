package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.CustomUserDetailsService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsServiceImpl(

    private val usuarioRepository: UsuarioRepository

) : CustomUserDetailsService {

    override fun loadUserByUsername(correo: String): UserDetails {

        val usuario = usuarioRepository.findByCorreo(correo)
            ?: throw UsernameNotFoundException("Usuario no encontrado")

        return User.builder()
            .username(usuario.correo)
            .password(usuario.password)
            .authorities(
                SimpleGrantedAuthority("ROLE_${usuario.rol.name}")
            )
            .build()
    }

}