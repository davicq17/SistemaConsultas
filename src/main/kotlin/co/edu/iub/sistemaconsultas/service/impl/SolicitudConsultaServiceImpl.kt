package co.edu.iub.sistemaconsultas.service.impl

import co.edu.iub.sistemaconsultas.dto.solicitud.*
import co.edu.iub.sistemaconsultas.exception.BadRequestException
import co.edu.iub.sistemaconsultas.exception.ResourceNotFoundException
import co.edu.iub.sistemaconsultas.mapper.toResponse
import co.edu.iub.sistemaconsultas.model.Modulo
import co.edu.iub.sistemaconsultas.model.RecursoFisico
import co.edu.iub.sistemaconsultas.model.SolicitudConsulta
import co.edu.iub.sistemaconsultas.model.Usuario
import co.edu.iub.sistemaconsultas.model.enums.EstadoSolicitud
import co.edu.iub.sistemaconsultas.model.enums.Rol
import co.edu.iub.sistemaconsultas.repository.ModuloRepository
import co.edu.iub.sistemaconsultas.repository.RecursoFisicoRepository
import co.edu.iub.sistemaconsultas.repository.SolicitudConsultaRepository
import co.edu.iub.sistemaconsultas.repository.UsuarioRepository
import co.edu.iub.sistemaconsultas.service.NotificacionService
import co.edu.iub.sistemaconsultas.service.SolicitudConsultaService
import co.edu.iub.sistemaconsultas.util.NumeroConsultaGenerator
import co.edu.iub.sistemaconsultas.util.SecurityUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class SolicitudConsultaServiceImpl(
    private val solicitudRepository: SolicitudConsultaRepository,
    private val usuarioRepository: UsuarioRepository,
    private val moduloRepository: ModuloRepository,
    private val recursoFisicoRepository: RecursoFisicoRepository,
    private val notificacionService: NotificacionService
) : SolicitudConsultaService {

    override fun registrar(request: RegistroSolicitudConsultaRequest): SolicitudConsultaResponse {
        if(request.fechaConsulta.isBefore(LocalDate.now())){
            throw BadRequestException(
                "La fecha de la consulta no puede ser anterior a la fecha actual."
            )
        }

        val estudiante = obtenerEstudiante()
        val docente = obtenerDocente(request.docenteId)
        val modulo = obtenerModulo(request.moduloId)
        val ultimoId = solicitudRepository.obtenerUltimoIdRegistrado()
        val numeroConsulta = NumeroConsultaGenerator.generar(ultimoId)

        val solicitudConsulta= SolicitudConsulta(
            numeroConsulta = numeroConsulta,
            prioridad = request.prioridad,
            asunto = request.asunto,
            descripcion = request.descripcion,
            fechaCreacion = LocalDateTime.now(),
            fechaConsulta = request.fechaConsulta,
            horaConsulta = request.horaConsulta,
            estudiante = estudiante,
            docente = docente,
            modulo = modulo,
            recursoFisico = null
        )

        val solicitudGuardada = solicitudRepository.save(solicitudConsulta)

        notificacionService.notificarNuevaSolicitud(solicitudGuardada)

        return solicitudGuardada.toResponse()
    }

    @Transactional(readOnly = true)
    override fun obtenerPorId(id: Long): SolicitudConsultaResponse {
        return obtenerSolicitud(id).toResponse()
    }

    @Transactional(readOnly = true)
    override fun listarTodas(): List<SolicitudConsultaResponse> {
        return solicitudRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun listarMisSolicitudes(): List<SolicitudConsultaResponse> {
        val usuario = obtenerUsuarioAuth()
        return solicitudRepository.findAllByEstudianteOrDocenteOrderByFechaCreacionDesc(
            usuario,
            usuario
        ).map { it.toResponse() }
    }

    override fun actualizar(id: Long, request: UpdateSolicitudConsultaRequest): SolicitudConsultaResponse {

        if(request.fechaConsulta.isBefore(LocalDate.now())){
            throw BadRequestException(
                "La fecha de la consulta no puede ser anterior a la fecha actual."
            )
        }

        val usuarioActual = obtenerUsuarioAuth()
        val solicitud = obtenerSolicitud(id)
        val modulo = obtenerModulo(request.moduloId)
        val fechaAnterior = solicitud.fechaConsulta
        val horaAnterior = solicitud.horaConsulta
        val destinatario = obtenerDestinatario(solicitud,usuarioActual)

        val cambioAgenda =
            fechaAnterior != request.fechaConsulta ||
                horaAnterior != request.horaConsulta

        if (cambioAgenda && request.motivo.isNullOrBlank()) {
            throw BadRequestException(
                "Debe indicar un motivo cuando se modifica la fecha o la hora de la consulta."
            )
        }

        solicitud.apply {
            prioridad = request.prioridad
            asunto = request.asunto
            descripcion = request.descripcion
            fechaConsulta = request.fechaConsulta
            horaConsulta = request.horaConsulta
            this.modulo = modulo
        }

        val solicitudGuardada = solicitudRepository.save(solicitud)

        if(fechaAnterior != solicitudGuardada.fechaConsulta ||
            horaAnterior != solicitudGuardada.horaConsulta
            ){
            notificacionService.notificarCambioAgenda(solicitudGuardada, destinatario, request.motivo!!)
        }

        return solicitudGuardada.toResponse()
    }

    override fun asignarRecursoFisico(id: Long, request: AsignarRecursoFisicoRequest): SolicitudConsultaResponse {
        val solicitud = obtenerSolicitud(id)
        if(!validarEstado(solicitud)){
            throw BadRequestException("No se puede asignar un recurso físico a esta consulta.")
        }

        solicitud.apply {
            recursoFisico = obtenerRecursoFisico(request.recursoFisicoId)
        }

        val solicitudGuardada = solicitudRepository.save(solicitud)

        notificacionService.notificarAsignacionRecurso(solicitudGuardada, solicitudGuardada.estudiante)

        return solicitudGuardada.toResponse()
    }

    override fun cambiarEstado(id: Long, request: CambiarEstadoSolicitudRequest): SolicitudConsultaResponse {
        val usuarioActual = obtenerUsuarioAuth()
        val  solicitud = obtenerSolicitud(id)
        validarCambioEstado(solicitud.estado, request.estado)
        validarMotivo(request.estado, request.motivo)

        solicitud.apply {
            estado = request.estado
        }

        val solicitudGuardada = solicitudRepository.save(solicitud)
        val destinatario = obtenerDestinatario(solicitudGuardada,usuarioActual)

        notificacionService.notificarCambioEstado(solicitudGuardada,destinatario, request.motivo)

        return solicitudGuardada.toResponse()
    }

    override fun reasignarDocente(id: Long, request: ReasignarDocenteRequest): SolicitudConsultaResponse {
        val solicitud = obtenerSolicitud(id)
        if(!validarEstado(solicitud)){
            throw BadRequestException("No se puede reasignar esta consulta.")
        }
        solicitud.apply {
            docente = obtenerDocente(request.docenteId)
        }

        val solicitudGuardada = solicitudRepository.save(solicitud)

        notificacionService.notificarReasignacion(solicitudGuardada, solicitudGuardada.docente)

        return solicitudGuardada.toResponse()
    }

    private fun obtenerUsuarioAuth(): Usuario{
        val correo = SecurityUtils.obtenerCorreo()

        return usuarioRepository.findByCorreoAndActivoTrue(correo)
            ?: throw ResourceNotFoundException("Usuario no encontrado.")
    }


    private fun obtenerEstudiante(): Usuario{
        val usuario = obtenerUsuarioAuth()
        if(usuario.rol != Rol.ESTUDIANTE){
            throw BadRequestException("El usuario indicado no tiene el rol ESTUDIANTE.")
        }
        return usuario
    }

    private fun obtenerDocente(id: Long): Usuario{
        val usuario = usuarioRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Docente no encontrado.")
        if(usuario.rol != Rol.DOCENTE){
            throw BadRequestException("El usuario indicado no tiene el rol DOCENTE.")
        }
        return usuario
    }

    private fun obtenerModulo(id: Long): Modulo{
        return moduloRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Módulo no encontrado.")
    }

    private fun obtenerSolicitud(id: Long): SolicitudConsulta {
        return  solicitudRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Solicitud no encontrada.") }
    }

    private fun obtenerRecursoFisico(id: Long): RecursoFisico{
        return recursoFisicoRepository.findByIdAndActivoTrue(id)
            ?: throw ResourceNotFoundException("Recurso físico no encontrado.")
    }

    private fun obtenerDestinatario(
        solicitud: SolicitudConsulta,
        usuarioActual: Usuario
    ): Usuario{

        return when (usuarioActual.id){
            solicitud.estudiante.id -> solicitud.docente
            solicitud.docente.id -> solicitud.estudiante
            else -> throw BadRequestException(
                "El usuario no pertenece a esta solicitud."
            )
        }
    }

    private fun validarEstado(consulta: SolicitudConsulta): Boolean{
        return consulta.estado == EstadoSolicitud.EN_PROCESO ||
                consulta.estado == EstadoSolicitud.PENDIENTE
    }

    private fun validarMotivo(estado: EstadoSolicitud, motivo: String?) {
        if (
            estado == EstadoSolicitud.RECHAZADA ||
            estado == EstadoSolicitud.CANCELADA
        ) {
            if (motivo.isNullOrBlank()) {
                throw BadRequestException("Debe indicar un motivo para $estado.")
            }
        }
    }

    private fun validarCambioEstado(
        estadoActual: EstadoSolicitud,
        nuevoEstado: EstadoSolicitud
    ){
        if(estadoActual == nuevoEstado){
            throw BadRequestException(
                "La solicitud ya se encuentra en estado $estadoActual.")
        }

        val estadosPermitidos = when (estadoActual){

            EstadoSolicitud.PENDIENTE -> setOf(
                EstadoSolicitud.EN_PROCESO,
                EstadoSolicitud.RECHAZADA,
                EstadoSolicitud.CANCELADA
            )

            EstadoSolicitud.EN_PROCESO -> setOf(
                EstadoSolicitud.RESUELTA,
                EstadoSolicitud.CANCELADA
            )

            EstadoSolicitud.RECHAZADA,
            EstadoSolicitud.CANCELADA,
            EstadoSolicitud.RESUELTA -> emptySet()
        }

        if(nuevoEstado !in estadosPermitidos){
            throw BadRequestException(
                "No se puede cambiar el estado de $estadoActual a $nuevoEstado"
            )
        }
    }
}