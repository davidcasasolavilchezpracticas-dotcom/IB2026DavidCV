package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ContractRepositoryRoom
 * Repositorio que gestiona los contratos directamente desde la base de datos local.
 */
class ContractRepositoryRoom @Inject constructor(
    private val _dao: ContractDao
) : ContractRepositoryInterface {

    override fun getContracts(forceRefresh: Boolean): Flow<BaseResult<List<Contract>>> =
        _dao.getAll().map { entities ->
            BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Contract>>
        }.catch { e ->
            emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
        }

    override suspend fun updateContractEmail(id: Int, email: String): BaseResult<Unit> {
        return try {
            _dao.updateEmail(id, email)
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractEmail(id: Int, email: String, status: ContractStatus): BaseResult<Unit> {
        return try {
            _dao.updateEmailAndStatus(id, email, status.name)
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractStatus(id: Int, status: ContractStatus): BaseResult<Unit> {
        return try {
            _dao.updateStatus(id, status.name)
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }
}
