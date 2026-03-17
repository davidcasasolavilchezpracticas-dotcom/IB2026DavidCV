package com.iberdrola.practicas2026.davidcv.domain.exception

/**
 * Gestiona las excepciones de contract que se pueden presentar en la aplicación
 */
sealed class ContractException (message: String?) : Exception( message ) {
    data object ConexionFailed: BillException("La conexión con el servidor ha fallado")
    data object DateInvalid: BillException("El rango de fechas debe de ser válido")
    data class UnknownError(val msg: String?): BillException(msg)
    data class ResponseError(val msg: String?): BillException(msg)
}