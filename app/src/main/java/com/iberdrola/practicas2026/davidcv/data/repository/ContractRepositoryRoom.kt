package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ContractRepositoryRoom
 * Repositorio que gestiona los contratos directamente desde la base de datos local.
 */
class ContractRepositoryRoom @Inject constructor(
    private val _dao: ContractDao
) : ContractRepositoryInterface {

    override fun getContracts(): Flow<BaseResult<List<Contract>>> = 
        _dao.getAll().map { entities ->
            BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Contract>>
        }.catch { e ->
            emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
        }
}
