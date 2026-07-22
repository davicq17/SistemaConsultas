package co.edu.iub.sistemaconsultas.controller

import co.edu.iub.sistemaconsultas.dto.UpdateUsuarioRequest
import co.edu.iub.sistemaconsultas.dto.UsuarioResponse
import co.edu.iub.sistemaconsultas.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/usuarios")
class UsuarioController (
    private val usuarioService: UsuarioService
){
    @GetMapping
    fun listarUsuarios(): List<UsuarioResponse>{
        return usuarioService.listarUsuarios()
    }

    @GetMapping("/{id}")
    fun obtenerUsuarioPorId(
        @PathVariable id: Long
    ): UsuarioResponse{
        return usuarioService.obtenerUsuarioPorId(id)
    }

    @PutMapping("/{id}")
    fun actualizarUsuario(
        @PathVariable id: Long,
        @RequestBody request: UpdateUsuarioRequest
    ): UsuarioResponse{
        return usuarioService.actualizarUsuario(id, request)
    }

    @DeleteMapping("/{id}")
    fun eliminarUsuario(
        @PathVariable id: Long
    ): ResponseEntity<Void>{

        usuarioService.eliminarUsuario(id)

        return ResponseEntity.noContent().build()
    }
}