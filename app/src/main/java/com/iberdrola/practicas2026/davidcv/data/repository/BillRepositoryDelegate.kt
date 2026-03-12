package com.iberdrola.practicas2026.davidcv.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepositoryDelegate @Inject constructor(
    private val networkRepository: BillRepositoryNetwork,
    private val roomRepository: BillRepositoryRoom
) : BillRepositoryInterface {

    private val currentRepository: BillRepositoryInterface
        get() = if (DataSourceConfig.useNetwork) networkRepository else roomRepository

    override fun getBills(): Flow<BaseResult<List<Bill>>>  = currentRepository.getBills()
    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = currentRepository.getBillsByType(type)
    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = currentRepository.getBillById(id)
    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> = currentRepository.updateBill(bill)
    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> = currentRepository.deleteBill(bill)
}
