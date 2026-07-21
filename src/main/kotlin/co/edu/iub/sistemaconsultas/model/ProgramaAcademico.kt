package co.edu.iub.sistemaconsultas.model

import jakarta.persistence.*

@Entity
@Table(name = "programas_academicos")
class ProgramaAcademico(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 150)
    var nombre: String = "",

    @OneToMany(mappedBy = "programa")
    var usuarios: MutableList<Usuario> = mutableListOf()

)