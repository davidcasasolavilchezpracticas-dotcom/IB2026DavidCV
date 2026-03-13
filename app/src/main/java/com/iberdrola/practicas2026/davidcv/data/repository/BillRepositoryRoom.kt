package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * BillRepositoryRoom
 * Implementacion del repositorio para room utilizando flujos reactivos
 */
class BillRepositoryRoom @Inject constructor(
    private val _dao: BillDao,
) : BillRepositoryInterface {

    override fun getBills(): Flow<BaseResult<List<Bill>>> =
        _dao.getAll().map { entities ->
            BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Bill>>
        }.catch { e ->
            emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
        }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> =
        _dao.getAllBillsByType(type).map { entities ->
            BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Bill>>
        }.catch { e ->
            emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
        }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> =
        _dao.getById(id).map { entity ->
            BaseResult.Success(entity.toModel()) as BaseResult<Bill>
        }.catch { e ->
            emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
        }.flowOn(Dispatchers.IO)

    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> = flow {
        try {
            _dao.update(bill.toEntity())
            emit(BaseResult.Success(bill))
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> = flow {
        try {
            _dao.delete(bill.toEntity())
            emit(BaseResult.Success(true))
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
