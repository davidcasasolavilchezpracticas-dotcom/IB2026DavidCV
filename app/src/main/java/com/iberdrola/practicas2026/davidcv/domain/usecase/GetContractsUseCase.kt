package com.iberdrola.practicas2026.davidcv.domain.usecase

import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetContractsUseCase
 * Caso de uso para obtener el listado de contratos
 */
class GetContractsUseCase @Inject constructor(
    private val _repository: ContractRepositoryInterface
) {
    operator fun invoke(): Flow<BaseResult<List<Contract>>> {
        return _repository.getContracts()
    }
}
