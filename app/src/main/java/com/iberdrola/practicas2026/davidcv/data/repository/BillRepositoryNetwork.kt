package com.iberdrola.practicas2026.davidcv.data.repository

import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult.Error
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

/**
 * BillRepositoryNetwork
 * Implementacion del repositorio para el network con validaciones de datos
 */
class BillRepositoryNetwork @Inject constructor(
    private val _apiService: ApiService
) : BillRepositoryInterface {

    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow {
        try {
            val response = _apiService.getBills()
            if (response.isSuccessful) {
                val body = response.body() ?: throw BillException.DataCorrupted
                val bills = body.map { it.toModel() }
                emit(BaseResult.Success(bills))
            } else {
                emit(Error(BillException.ResponseError("Error GET: ${response.code()}")))
            }
        } catch (e: IOException) {
            emit(Error(BillException.ConexionFailed))
        } catch (e: BillException) {
            emit(Error(e))
        } catch (e: Exception) {
            emit(Error(BillException.UnknownError(e.message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = flow {
        try {
            val response = _apiService.getBills()
            if (response.isSuccessful) {
                val body = response.body() ?: throw BillException.DataCorrupted
                val bills = body
                    .filter { it.type == type }
                    .map { it.toModel() }
                emit(BaseResult.Success(bills))
            } else {
                emit(Error(BillException.ResponseError("Error GET Type: ${response.code()}")))
            }
        } catch (e: IOException) {
            emit(Error(BillException.ConexionFailed))
        } catch (e: BillException) {
            emit(Error(e))
        } catch (e: Exception) {
            emit(Error(BillException.UnknownError(e.message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow {
        try {
            val response = _apiService.getBillById(id)
            if (response.isSuccessful) {
                val body = response.body() ?: throw BillException.DataCorrupted
                emit(BaseResult.Success(body.toModel()))
            } else {
                emit(Error(BillException.ResponseError("Error GET ID: ${response.code()}")))
            }
        } catch (e: IOException) {
            emit(Error(BillException.ConexionFailed))
        } catch (e: BillException) {
            emit(Error(e))
        } catch (e: Exception) {
            emit(Error(BillException.UnknownError(e.message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateBill(bill: Bill): Flow<BaseResult<Bill>> = flow {
        try {
            val response = _apiService.updateBill(bill.id, bill.toEntity())
            if (response.isSuccessful) {
                val body = response.body() ?: throw BillException.DataCorrupted
                emit(BaseResult.Success(body.toModel()))
            } else {
                emit(Error(BillException.ResponseError("Error PUT: ${response.code()}")))
            }
        } catch (e: BillException) {
            emit(Error(e))
        } catch (e: Exception) {
            emit(Error(BillException.UnknownError(e.message)))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteBill(bill: Bill): Flow<BaseResult<Boolean>> = flow {
        try {
            val response = _apiService.deleteBill(bill.id)
            if (response.isSuccessful) {
                emit(BaseResult.Success(true))
            } else {
                emit(Error(BillException.ResponseError("Error DELETE: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Error(BillException.UnknownError(e.message)))
        }
    }.flowOn(Dispatchers.IO)
}
