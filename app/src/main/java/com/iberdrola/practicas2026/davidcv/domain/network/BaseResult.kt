package com.iberdrola.practicas2026.davidcv.domain.network

/**
 * BaseResult
 * Gestiona los posibles resultados de una petición
 */
sealed class BaseResult <out T> {

    data class Success<T> ( var data: T ): BaseResult<T>()

    data class Error (var exception: Exception): BaseResult<Nothing>()
}