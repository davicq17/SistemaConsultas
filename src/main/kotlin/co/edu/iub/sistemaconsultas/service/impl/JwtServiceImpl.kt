package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.config.JwtProperties
import co.edu.iub.sistemaconsultas.service.JwtService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.Date
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtServiceImpl(
    private val jwtProperties: JwtProperties
) : JwtService {

    override fun generarToken(userDetails: UserDetails): String {
        println("JWT SECRET = '${jwtProperties.secret}'")
        return generarTokenInterno(emptyMap(), userDetails)
    }

    override fun extraerCorreo(token: String): String {

        return extraerClaim(token) {it.subject}
    }

    override fun esTokenValido(
        token: String,
        userDetails: UserDetails
    ): Boolean {

        val correo = extraerCorreo(token)

        return correo == userDetails.username && !tokenExpirado(token)
    }

    private fun tokenExpirado(token: String): Boolean {

        return extraerExpiracion(token).before(Date())
    }

    private fun generarTokenInterno(
        claims: Map<String, Any>,
        userDetails: UserDetails
    ): String {

        val ahora = Date()

        val expiracion = Date(
            ahora.time + jwtProperties.expirationMinutes * 60 * 1000
        )

        return Jwts.builder()
            .claims(claims)
            .subject(userDetails.username)
            .issuedAt(ahora)
            .expiration(expiracion)
            .signWith(obtenerClaveSecreta())
            .compact()
    }

    private fun extraerExpiracion(token: String): Date{

        return extraerClaim(token) {it.expiration}
    }

    private fun obtenerClaveSecreta(): SecretKey {

        return Keys.hmacShaKeyFor(
            jwtProperties.secret.toByteArray()
        )
    }

    private fun <T> extraerClaim(
        token: String,
        claimResolver: Function<Claims, T>
    ): T {
        val claims = Jwts.parser()
            .verifyWith(obtenerClaveSecreta())
            .build()
            .parseSignedClaims(token)
            .payload

        return claimResolver.apply(claims)
    }

}

