package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name ="sedes")
class Sede (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 100)
    var nombre: String = "",

    @Column(nullable = false)
    var activo: Boolean = true,

    @OneToMany(mappedBy = "sede", fetch = FetchType.LAZY)
    var bloques: MutableList<Bloque> = mutableListOf()
)