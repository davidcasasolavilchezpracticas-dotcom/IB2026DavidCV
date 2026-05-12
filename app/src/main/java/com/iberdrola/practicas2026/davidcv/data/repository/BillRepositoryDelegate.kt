package com.iberdrola.practicas2026.davidcv.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BillRepositoryDelegate
 * Repositorio que hace de nexo entre ambos para poder hacer uso de los dos indistintamente.
 * Implementa la estrategia de Single Source of Truth usando Room con protección contra race conditions.
 */
@Singleton
class BillRepositoryDelegate @Inject constructor(
    private val _apiService: ApiService,
    private val _dao: BillDao,
    private val _gson: Gson,
    @ApplicationContext private val _context: Context
) : BillRepositoryInterface {

    private val syncMutex = Mutex()

    /**
     * Sincroniza los datos según la configuración (Network o Mock JSON)
     * y los guarda en Room. Protegido por Mutex para evitar parpadeos de "no data".
     */
    private suspend fun syncBills() = syncMutex.withLock {
        try {
            val billsToInsert: List<BillEntity>
            if (DataSourceConfig.useNetwork) {
                val response = try {
                    _apiService.getBills()
                } catch (e: IOException) {
                    throw BillException.ConexionFailed
                }

                if (response.isSuccessful) {
                    val body = response.body() ?: throw BillException.DataCorrupted
                    billsToInsert = try {
                        body.map { it.toModel().toEntity() }
                    } catch (e: Exception) {
                        throw BillException.DataCorrupted
                    }

                    _dao.clearAndInsert(billsToInsert)
                } else {
                    throw BillException.ResponseError("Error RED: ${response.code()}")
                }
            } else {
                val jsonString = try {
                    _context.assets.open("BillJSON.json").bufferedReader().use { it.readText() }
                } catch (e: IOException) {
                    throw BillException.ResponseError("No se pudo leer el archivo mock local")
                }

                val type = object : TypeToken<List<BillEntity>>() {}.type

                val entities: List<BillEntity> = try {
                    _gson.fromJson(jsonString, type)
                } catch (e: JsonSyntaxException) {
                    throw BillException.DataCorrupted
                }

                billsToInsert = try {
                    entities.map { it.toModel().toEntity() }
                } catch (e: Exception) {
                    throw BillException.DataCorrupted
                }

                _dao.clearAndInsert(billsToInsert)
            }
        } catch (e: BillException) {
            Log.e("BillRepository", "Error controlado: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("BillRepository", "Excepción no controlada: ${e}")
            throw BillException.UnknownError(e.message)
        }
    }


    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow<BaseResult<List<Bill>>> {
        syncBills()
        emitAll(
            _dao.getAll().map { entities ->
                val bills: List<Bill> = entities.map { it.toModel() }
                BaseResult.Success(bills)
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(e as? BillException ?: BillException.UnknownError(e.message)))
    }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = flow<BaseResult<List<Bill>>> {
        syncBills()
        emitAll(
            _dao.getAllBillsByType(type).map { entities ->
                val bills: List<Bill> = entities.map { it.toModel() }
                BaseResult.Success(bills)
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(e as? BillException ?: BillException.UnknownError(e.message)))
    }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow<BaseResult<Bill>> {
        _dao.getById(id).collect { entity ->
            emit(BaseResult.Success(entity.toModel()))
        }
    }.catch { e ->
        emit(BaseResult.Error(e as? BillException ?: BillException.UnknownError(e.message)))
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
