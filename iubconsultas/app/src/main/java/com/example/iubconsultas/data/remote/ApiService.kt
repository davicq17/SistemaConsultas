package com.example.iubconsultas.data.remote

import com.example.iubconsultas.data.remote.dto.auth.LoginRequest
import com.example.iubconsultas.data.remote.dto.auth.LoginResponse
import com.example.iubconsultas.data.remote.dto.auth.RegisterRequest
import com.example.iubconsultas.data.remote.dto.catalog.BlockResponse
import com.example.iubconsultas.data.remote.dto.comment.CommentResponse
import com.example.iubconsultas.data.remote.dto.comment.CreateCommentRequest
import com.example.iubconsultas.data.remote.dto.comment.UpdateCommentRequest
import com.example.iubconsultas.data.remote.dto.consultation.AssignResourceRequest
import com.example.iubconsultas.data.remote.dto.consultation.ChangeStatusRequest
import com.example.iubconsultas.data.remote.dto.consultation.ConsultationResponse
import com.example.iubconsultas.data.remote.dto.consultation.CreateConsultationRequest
import com.example.iubconsultas.data.remote.dto.consultation.ReassignTeacherRequest
import com.example.iubconsultas.data.remote.dto.consultation.UpdateConsultationRequest
import com.example.iubconsultas.data.remote.dto.catalog.CreateBlockRequest
import com.example.iubconsultas.data.remote.dto.catalog.CreateModuleRequest
import com.example.iubconsultas.data.remote.dto.catalog.CreateProgramRequest
import com.example.iubconsultas.data.remote.dto.catalog.CreateResourceRequest
import com.example.iubconsultas.data.remote.dto.catalog.CreateSedeRequest
import com.example.iubconsultas.data.remote.dto.catalog.ModuleResponse
import com.example.iubconsultas.data.remote.dto.catalog.ProgramResponse
import com.example.iubconsultas.data.remote.dto.catalog.ResourceResponse
import com.example.iubconsultas.data.remote.dto.catalog.SedeResponse
import com.example.iubconsultas.data.remote.dto.catalog.UpdateBlockRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateModuleRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateProgramRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateResourceRequest
import com.example.iubconsultas.data.remote.dto.catalog.UpdateSedeRequest
import com.example.iubconsultas.data.remote.dto.user.UpdateUserRequest
import com.example.iubconsultas.data.remote.dto.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<String>

    @GET("usuarios")
    suspend fun getUsers(): Response<List<UserResponse>>

    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") id: Long): Response<UserResponse>

    @PUT("usuarios/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body request: UpdateUserRequest
    ): Response<UserResponse>

    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: Long): Response<Unit>

    @GET("solicitudes-consultas")
    suspend fun getConsultations(): Response<List<ConsultationResponse>>

    @POST("solicitudes-consultas")
    suspend fun createConsultation(
        @Body request: CreateConsultationRequest
    ): Response<ConsultationResponse>

    @PUT("solicitudes-consultas/{id}")
    suspend fun updateConsultation(
        @Path("id") id: Long,
        @Body request: UpdateConsultationRequest
    ): Response<ConsultationResponse>

    @PATCH("solicitudes-consultas/{id}/estado")
    suspend fun changeConsultationStatus(
        @Path("id") id: Long,
        @Body request: ChangeStatusRequest
    ): Response<ConsultationResponse>

    @PATCH("solicitudes-consultas/{id}/recurso-fisico")
    suspend fun assignConsultationResource(
        @Path("id") id: Long,
        @Body request: AssignResourceRequest
    ): Response<ConsultationResponse>

    @PATCH("solicitudes-consultas/{id}/docente")
    suspend fun reassignConsultationTeacher(
        @Path("id") id: Long,
        @Body request: ReassignTeacherRequest
    ): Response<ConsultationResponse>

    @GET("comentarios/solicitud/{solicitudId}")
    suspend fun getCommentsByConsultation(
        @Path("solicitudId") consultationId: Long
    ): Response<List<CommentResponse>>

    @POST("comentarios")
    suspend fun createComment(
        @Body request: CreateCommentRequest
    ): Response<CommentResponse>

    @PUT("comentarios/{id}")
    suspend fun updateComment(
        @Path("id") id: Long,
        @Body request: UpdateCommentRequest
    ): Response<CommentResponse>

    @POST("programas")
    suspend fun createProgram(@Body request: CreateProgramRequest): Response<ProgramResponse>

    @GET("programas")
    suspend fun getPrograms(): Response<List<ProgramResponse>>

    @PUT("programas/{id}")
    suspend fun updateProgram(
        @Path("id") id: Long,
        @Body request: UpdateProgramRequest
    ): Response<ProgramResponse>

    @DELETE("programas/{id}")
    suspend fun deleteProgram(@Path("id") id: Long): Response<Unit>

    @POST("modulos")
    suspend fun createModule(@Body request: CreateModuleRequest): Response<ModuleResponse>

    @GET("modulos")
    suspend fun getModules(): Response<List<ModuleResponse>>

    @PUT("modulos/{id}")
    suspend fun updateModule(
        @Path("id") id: Long,
        @Body request: UpdateModuleRequest
    ): Response<ModuleResponse>

    @DELETE("modulos/{id}")
    suspend fun deleteModule(@Path("id") id: Long): Response<Unit>

    @POST("sedes")
    suspend fun createSede(@Body request: CreateSedeRequest): Response<SedeResponse>

    @GET("sedes")
    suspend fun getSedes(): Response<List<SedeResponse>>

    @PUT("sedes/{id}")
    suspend fun updateSede(
        @Path("id") id: Long,
        @Body request: UpdateSedeRequest
    ): Response<SedeResponse>

    @DELETE("sedes/{id}")
    suspend fun deleteSede(@Path("id") id: Long): Response<Unit>

    @POST("bloques")
    suspend fun createBlock(@Body request: CreateBlockRequest): Response<BlockResponse>

    @GET("bloques")
    suspend fun getBlocks(): Response<List<BlockResponse>>

    @PUT("bloques/{id}")
    suspend fun updateBlock(
        @Path("id") id: Long,
        @Body request: UpdateBlockRequest
    ): Response<BlockResponse>

    @DELETE("bloques/{id}")
    suspend fun deleteBlock(@Path("id") id: Long): Response<Unit>

    @POST("recursos-fisicos")
    suspend fun createResource(@Body request: CreateResourceRequest): Response<ResourceResponse>

    @GET("recursos-fisicos")
    suspend fun getResources(): Response<List<ResourceResponse>>

    @PUT("recursos-fisicos/{id}")
    suspend fun updateResource(
        @Path("id") id: Long,
        @Body request: UpdateResourceRequest
    ): Response<ResourceResponse>

    @DELETE("recursos-fisicos/{id}")
    suspend fun deleteResource(@Path("id") id: Long): Response<Unit>
}