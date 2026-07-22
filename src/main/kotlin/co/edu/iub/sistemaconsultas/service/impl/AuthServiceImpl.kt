package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.ForgotPasswordRequest
import co.edu.iub.sistemaconsultas.dto.LoginRequest
import co.edu.iub.sistemaconsultas.dto.LoginResponse
import co.edu.iub.sistemaconsultas.dto.RegistroUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.ResetPasswordRequest
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.model.PasswordResetToken
import co.edu.iub.sistemaconsultas.model.Rol
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.repository.PasswordResetTokenRepository
import co.edu.iub.sistemaconsultas.repository.ProgAcademicoRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.AuthService
import co.edu.iub.sistemaconsultas.service.CustomUserDetailsService
import co.edu.iub.sistemaconsultas.service.EmailService
import co.edu.iub.sistemaconsultas.service.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val customUserDetailsService: CustomUserDetailsService,
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val programaRepository: ProgAcademicoRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val emailService: EmailService
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

    @Transactional
    override fun forgotPassword(request: ForgotPasswordRequest) {

        val usuario = usuarioRepository.findByCorreo(request.correo)
            ?: throw BadRequestException("No existe un usuario registrado con ese correo.")

        passwordResetTokenRepository.deleteByUsuario(usuario)

        val token = UUID.randomUUID().toString()

        val passwordResetToken = PasswordResetToken(
            token = token,
            usuario = usuario,
            fechaExpiracion = LocalDateTime.now().plusMinutes(30),
            utilizado = false
        )

        passwordResetTokenRepository.save(passwordResetToken)

        emailService.enviarCorreo(
            destinatario = usuario.correo,
            asunto = "Recuperación de contraseña",
            contenido = """
            Hola ${usuario.nombre},

            Has solicitado recuperar tu contraseña.

            Utiliza el siguiente token para restablecerla:

            $token

            Este token expira en 30 minutos.
        """.trimIndent()
        )
    }

    @Transactional
    override fun resetPassword(request: ResetPasswordRequest) {

        val passwordResetToken = passwordResetTokenRepository.findByToken(request.token)
            ?: throw BadRequestException("El token no es válido.")

        if (passwordResetToken.utilizado) {
            throw BadRequestException("El token ya fue utilizado.")
        }

        if (passwordResetToken.fechaExpiracion.isBefore(LocalDateTime.now())) {
            throw BadRequestException("El token ha expirado.")
        }

        val usuario = passwordResetToken.usuario

        val passwordCodificada = passwordEncoder.encode(request.nuevaPassword)
            ?: throw IllegalStateException("No fue posible codificar la contraseña.")

        usuario.password = passwordCodificada
        //usuario.password = passwordEncoder.encode(request.nuevaPassword)!!

        usuarioRepository.save(usuario)

        passwordResetToken.utilizado = true

        passwordResetTokenRepository.save(passwordResetToken)
    }
}