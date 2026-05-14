package com.iberdrola.practicas2026.davidcv.domain.exception

/**
 * Gestiona las excepciones de contract que se pueden presentar en la aplicación
 */
sealed class ContractException (message: String?) : Exception( message ) {
    data object ConexionFailed: ContractException("La conexión con el servidor ha fallado")
    data object DataCorrupted: ContractException("Los datos recibidos no son correctos")
    data class UnknownError(val msg: String?): ContractException(msg)
    data class ResponseError(val msg: String?): ContractException(msg)
}