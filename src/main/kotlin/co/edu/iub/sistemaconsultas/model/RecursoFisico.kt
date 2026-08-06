package co.edu.iub.sistemaconsultas.model

import co.edu.iub.sistemaconsultas.model.enums.TipoRecursoFisico
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "recursos_fisicos",)
class RecursoFisico (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var nombre: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoRecursoFisico = TipoRecursoFisico.SALON,

    @Column(nullable = false)
    var activo: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloque_id", nullable = false)
    var bloque: Bloque
)