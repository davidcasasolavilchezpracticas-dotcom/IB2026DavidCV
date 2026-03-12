package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class BillRepositoryMock @Inject constructor() : BillRepositoryInterface {

    val now = LocalDateTime.now()

    private val mockBills = listOf(
        Bill(id = 1, type = BillType.GAS, value = 20, startDate = now.minusMonths(1), endDate = now, paymentStatus = true),
        Bill(id = 2, type = BillType.GAS, value = 60, startDate = now.minusMonths(2), endDate = now.minusMonths(1), paymentStatus = false),
        Bill(id = 3, type = BillType.GAS, value = 20, startDate = now.minusMonths(3), endDate = now.minusMonths(2), paymentStatus = true),
        Bill(id = 4, type = BillType.LIGHT, value = 60, startDate = now.minusMonths(1), endDate = now, paymentStatus = false),
        Bill(id = 5, type = BillType.LIGHT, value = 150, startDate = now.minusYears(1).minusMonths(1), endDate = now.minusYears(1), paymentStatus = true),
        Bill(id = 6, type = BillType.LIGHT, value = 150, startDate = now.minusMonths(2), endDate = now.minusMonths(1), paymentStatus = true),
        Bill(id = 7, type = BillType.LIGHT, value = 150, startDate = now.minusYears(1).minusMonths(2), endDate = now.minusYears(1).minusMonths(1), paymentStatus = true),
        Bill(id = 8, type = BillType.LIGHT, value = 150, startDate = now.minusYears(2).minusMonths(1), endDate = now.minusYears(2).minusMonths(2), paymentStatus = true)
    )

    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow {
        emit(BaseResult.Success(mockBills))
    }

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = flow {
        emit(BaseResult.Success(mockBills.filter { it.type == type }))
    }

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow {
        val bill = mockBills.find { it.id == id }
        if (bill != null) emit(BaseResult.Success(bill))
        else emit(BaseResult.Error(Exception("Bill not found")))
    }

    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> = flow { emit(BaseResult.Success(bill)) }

    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> = flow { emit(BaseResult.Success(true)) }
}
