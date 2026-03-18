package com.iberdrola.practicas2026.davidcv.domain.usecase

import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * GetContractByIdUseCase
 * Caso de uso para obtener un contrato por su ID
 */
class GetContractByIdUseCase @Inject constructor(
    private val _repository: ContractRepositoryInterface
) {
    operator fun invoke(id: Int): Flow<BaseResult<Contract>> {
        return _repository.getContracts().map { result ->
            when (result) {
                is BaseResult.Success -> {
                    val contract = result.data.find { it.id == id }
                    if (contract != null) {
                        BaseResult.Success(contract)
                    } else {
                        BaseResult.Error(Exception("Contrato no encontrado"))
                    }
                }
                is BaseResult.Error -> BaseResult.Error(result.exception)
            }
        }
    }
}
