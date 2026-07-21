package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.*

@Entity
@Table(name = "modulos")
class Modulo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 150)
    var nombre: String = "",

    @Column(length = 500)
    var descripcion: String = ""

)