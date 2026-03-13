package com.iberdrola.practicas2026.davidcv.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
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
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BillRepositoryDelegate
 * Repositorio que hace de nexo entre ambos para poder hacer uso de los dos indistintamente.
 * Implementa la estrategia de Single Source of Truth usando Room.
 */
@Singleton
class BillRepositoryDelegate @Inject constructor(
    private val _apiService: ApiService,
    private val _dao: BillDao,
    private val _gson: Gson,
    @ApplicationContext private val _context: Context
) : BillRepositoryInterface {

    /**
     * Sincroniza los datos según la configuración (Network o Mock JSON)
     * y los guarda en Room.
     */
    private suspend fun syncBills() {
        try {
            val billsToInsert: List<BillEntity> = if (DataSourceConfig.useNetwork) {
                Log.d("BillRepository", "Intentando sincronizar desde RED...")
                val response = _apiService.getBills()
                if (response.isSuccessful && response.body() != null) {
                    Log.d("BillRepository", "Sincronización de RED exitosa: ${response.body()?.size} elementos")
                    response.body()!!.map { it.toModel().toEntity() }
                } else {
                    Log.e("BillRepository", "Error en RED: ${response.code()} ${response.errorBody()?.string()}")
                    throw BillException.ConexionFailed
                }
            } else {
                Log.d("BillRepository", "Sincronizando desde MOCK LOCAL...")
                val jsonString = _context.assets.open("BillJSON.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<BillEntity>>() {}.type
                _gson.fromJson(jsonString, type)
            }

            if (billsToInsert.isNotEmpty()) {
                _dao.deleteAll()
                _dao.insertAll(billsToInsert)
                Log.d("BillRepository", "Base de datos actualizada con ${billsToInsert.size} facturas")
            }
        } catch (e: Exception) {
            Log.e("BillRepository", "Excepción durante la sincronización: ${e.message}")
            throw BillException.ConexionFailed
        }
    }

    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow {
        syncBills()
        emitAll(
            _dao.getAll().map { entities ->
                BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Bill>>
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
    }.flowOn(Dispatchers.IO)

    override fun getBillsByType(type: BillType): Flow<BaseResult<List<Bill>>> = flow {
        syncBills()
        emitAll(
            _dao.getAllBillsByType(type).map { entities ->
                BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Bill>>
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
    }.flowOn(Dispatchers.IO)

    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow {
        emitAll(
            _dao.getById(id).map { entity ->
                BaseResult.Success(entity.toModel()) as BaseResult<Bill>
            }
        )
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