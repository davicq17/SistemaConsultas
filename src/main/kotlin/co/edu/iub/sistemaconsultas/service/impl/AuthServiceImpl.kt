package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.model.Rol
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.repository.ProgramaAcademicoRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.AuthService
import co.edu.iub.sistemaconsultas.service.CustomUserDetailsService
import co.edu.iub.sistemaconsultas.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val customUserDetailsService: CustomUserDetailsService,
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val programaRepository: ProgramaAcademicoRepository
): AuthService {

    override fun login(request: LoginRequest): LoginResponse {

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.correo,

                request.password
            )
        )

        val userDetails = customUserDetailsService
            .loadUserByUsername(request.correo)

        val token = jwtService.generarToken(userDetails)

        return LoginResponse(token)
    }

    override fun registrar(request: RegistroUsuarioRequest) {

        if(usuarioRepository.existsByCorreo(request.correo)){
            throw IllegalArgumentException("El correo ya está registrado")
        }

        if(usuarioRepository.existsByIdentificacion(request.identificacion)){
            throw IllegalArgumentException("La identificación ya está registrada.")
        }

        val programa = request.programaId?.let {

            programaRepository.findById(it)
                .orElseThrow{
                    IllegalArgumentException("Programa académico no encontrado")
                }
        }

        val usuario = Usuario(
            identificacion = request.identificacion,
            nombre = request.nombre,
            apellido = request.apellido,
            correo = request.correo,
            password = requireNotNull(passwordEncoder.encode(request.password)),
            rol = Rol.valueOf(request.rol.uppercase()),
            programa = programa
        )

        usuarioRepository.save(usuario)
    }
}