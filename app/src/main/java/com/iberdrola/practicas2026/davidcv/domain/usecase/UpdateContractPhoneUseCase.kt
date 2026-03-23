package com.iberdrola.practicas2026.davidcv.domain.usecase

import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import javax.inject.Inject

/**
 * UpdateContractPhoneUseCase
 * Caso de uso para actualizar el número de teléfono de un contrato
 */
class UpdateContractPhoneUseCase @Inject constructor(
    private val _repository: ContractRepositoryInterface
) {
    suspend operator fun invoke(id: Int, phone: String): BaseResult<Unit> {
        return _repository.updateContractPhone(id, phone)
    }
}
