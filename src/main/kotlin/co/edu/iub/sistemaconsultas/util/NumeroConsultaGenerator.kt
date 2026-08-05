package co.edu.iub.sistemaconsultas.util

import java.time.Year

object NumeroConsultaGenerator {

    fun generar(ultimoId: Long): String{

        val siguienteConsecutivo = ultimoId +1

        val anio = Year.now().value

        return "SC-$anio-${siguienteConsecutivo.toString().padStart(6,'0')}"
    }
}