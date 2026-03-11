package com.iberdrola.practicas2026.davidcv.domain.repository

import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import kotlinx.coroutines.flow.Flow

interface BillRepositoryInterface {
    fun getBills(): Flow<BaseResult<List<Bill>>>
    fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>>

    fun getBillById(id: Int): Flow<BaseResult<Bill>>

    fun updateBill(bill: Bill): Flow<BaseResult<Bill>>

    fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>>
}
