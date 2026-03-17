package com.iberdrola.practicas2026.davidcv.domain.repository

import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import kotlinx.coroutines.flow.Flow

/**
 * ContractRepositoryInterface
 * Interfaz para el repositorio de contratos
 */
interface ContractRepositoryInterface {
    fun getContracts(): Flow<BaseResult<List<Contract>>>
}
