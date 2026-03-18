package com.iberdrola.practicas2026.davidcv.domain.usecase


import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import javax.inject.Inject

/**
 * UpdateContractEmailUseCase
 * Caso de uso para actualizar el email de un contrato
 */
class UpdateContractEmailAndStatusUseCase @Inject constructor(
    private val _repository: ContractRepositoryInterface
) {
    suspend operator fun invoke(id: Int, email: String, status: ContractStatus): BaseResult<Unit> {
        return _repository.updateContractEmail(id, email, status)
    }
}