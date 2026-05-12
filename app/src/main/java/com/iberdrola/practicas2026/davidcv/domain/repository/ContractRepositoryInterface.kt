package com.iberdrola.practicas2026.davidcv.domain.repository

import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import kotlinx.coroutines.flow.Flow

/**
 * ContractRepositoryInterface
 * Interfaz para el repositorio de contratos
 */
interface ContractRepositoryInterface {
    fun getContracts(): Flow<BaseResult<List<Contract>>>
    suspend fun updateContractEmail(id: Int, email: String): BaseResult<Unit>
    suspend fun updateContractEmail(id: Int, email: String, status: ContractStatus): BaseResult<Unit>
    suspend fun updateContractStatus(id: Int, status: ContractStatus): BaseResult<Unit>
    suspend fun updateContractPhone(id: Int, phone: String): BaseResult<Unit>
}
