package com.iberdrola.practicas2026.davidcv.domain.exception

/**
 * Gestiona las excepciones que se pueden presentar en la aplicación
 */
sealed class BillException (message: String?) : Exception( message ) {
    data object ConexionFailed: BillException("La conexión con el servidor ha fallado")
    data object DateInvalid: BillException("El rango de fechas debe de ser válido")
    data object DataCorrupted: BillException("Los datos recibidos del servidor no tienen el formato correcto")
    data class UnknownError(val msg: String?): BillException(msg)
    data class ResponseError(val msg: String?): BillException(msg)
}
