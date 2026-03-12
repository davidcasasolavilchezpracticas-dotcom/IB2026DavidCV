package com.iberdrola.practicas2026.davidcv.domain.usecase

import com.iberdrola.practicas2026.davidcv.data.repository.BillRepositoryNetwork
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLightBillsUseCase @Inject constructor(
    private val _repository: BillRepositoryInterface
) {
    operator fun invoke() : Flow<BaseResult<List<Bill>>> {
        return _repository.getBillsByType(type = BillType.LIGHT)
    }
}
