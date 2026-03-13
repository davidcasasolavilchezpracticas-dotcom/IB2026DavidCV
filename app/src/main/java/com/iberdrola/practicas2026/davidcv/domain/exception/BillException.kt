package com.iberdrola.practicas2026.davidcv.domain.exception

/**
 * Gestiona las excepciones que se pueden presentar en la aplicación
 */
sealed class BillException (message: String?) : Exception( message ) {
    data object ConexionFailed: BillException("La conexión con el servidor ha fallado")
    data class UnknownError(val msg: String?): BillException(msg)
    data class ResponseError(val msg: String?): BillException(msg)
}