package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BillRepositoryImplementation @Inject constructor(
    private val _dao: BillDao,
) : BillRepositoryInterface {
    override fun getBills(): Flow<BaseResult<List<Bill>>> =
        flow<BaseResult<List<Bill>>> {
            try {
                val listBill = _dao.getAll().map { it.toModel() }
                emit(BaseResult.Success(listBill))
            } catch (e: Exception) {
                emit(BaseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> =
        flow<BaseResult<List<Bill>>> {
            try {
                val listBill = _dao.getAllBillsByType(type = type).map { it.toModel() }
                emit(BaseResult.Success(listBill))
            } catch (e: Exception) {
                emit(BaseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> =
        flow<BaseResult<Bill>> {
            try {
                val bill = _dao.getById(id).toModel()
                emit(BaseResult.Success(bill))
            } catch (e: Exception) {
                emit(BaseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> =
        flow<BaseResult<Bill>> {
            try {
                _dao.update(bill.toEntity())
                emit(BaseResult.Success(bill))
            } catch (e: Exception) {
                emit(BaseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> =
        flow<BaseResult<Boolean>> {
            try {
                _dao.delete(bill.toEntity())
                emit(BaseResult.Success(true))
            } catch (e: Exception) {
                emit(BaseResult.Error(e))
            }
        }.flowOn(Dispatchers.IO)
}
