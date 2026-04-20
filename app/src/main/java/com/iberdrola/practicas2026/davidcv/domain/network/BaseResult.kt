package com.iberdrola.practicas2026.davidcv.domain.network

/**
 * BaseResult
 * Gestiona los posibles resultados de una petición
 */
sealed class BaseResult <out T> {

    data class Success<T> ( val data: T ): BaseResult<T>()

    data class Error (val exception: Exception): BaseResult<Nothing>()
}