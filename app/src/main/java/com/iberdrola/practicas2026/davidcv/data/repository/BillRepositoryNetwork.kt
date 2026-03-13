package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * BillRepositoryNetwork
 * Implementacion del repositorio para el network
 */
class BillRepositoryNetwork @Inject constructor(
    private val _apiService: ApiService
) : BillRepositoryInterface {

    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow {
        try {
            val response = _apiService.getBills()
            if (response.isSuccessful && response.body() != null) {
                val bills = response.body()!!.map { it.toModel() }
                emit(BaseResult.Success(bills))
            } else {
                emit(BaseResult.Error(Exception("Error GET: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = flow {
        try {
            val response = _apiService.getBills()
            if (response.isSuccessful && response.body() != null) {
                val bills = response.body()!!
                    .filter { it.type == type }
                    .map { it.toModel() }
                emit(BaseResult.Success(bills))
            } else {
                emit(BaseResult.Error(Exception("Error GET Type: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow {
        try {
            val response = _apiService.getBillById(id)
            if (response.isSuccessful && response.body() != null) {
                emit(BaseResult.Success(response.body()!!.toModel()))
            } else {
                emit(BaseResult.Error(Exception("Error GET ID: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> = flow {
        try {
            val response = _apiService.updateBill(bill.id, bill.toEntity())
            if (response.isSuccessful && response.body() != null) {
                emit(BaseResult.Success(response.body()!!.toModel()))
            } else {
                emit(BaseResult.Error(Exception("Error PUT: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> = flow {
        try {
            val response = _apiService.deleteBill(bill.id)
            if (response.isSuccessful) {
                emit(BaseResult.Success(true))
            } else {
                emit(BaseResult.Error(Exception("Error DELETE: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(BaseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
