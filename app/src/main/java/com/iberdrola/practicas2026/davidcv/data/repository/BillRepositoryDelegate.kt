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
import java.io.IOException
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
            val billsToInsert: List<BillEntity>
            if (DataSourceConfig.useNetwork) {
                Log.d("ComprobacionesBillRepository", "Intentando sincronizar desde RED...")
                val response = _apiService.getBills()
                if (response.isSuccessful) {
                    val body = response.body() ?: throw BillException.DataCorrupted
                    billsToInsert = body.map { it.toModel().toEntity() }
                } else {
                    Log.e("ComprobacionesBillRepository", "Error en RED: ${response.code()}")
                    throw BillException.ResponseError("Error RED: ${response.code()}")
                }
            } else {
                Log.d("ComprobacionesBillRepository", "Sincronizando desde MOCK LOCAL...")
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

                billsToInsert = entities.map { it.toModel().toEntity() }
            }

            if (billsToInsert.isNotEmpty()) {
                _dao.deleteAll()
                _dao.insertAll(billsToInsert)
                Log.d("ComprobacionesBillRepository", "Base de datos actualizada con ${billsToInsert.size} facturas")
            }

        } catch (e: BillException) {
            Log.e("ComprobacionesBillRepository", "Error controlado: ${e.message}")
            throw e
        }catch (e: Exception) {
            Log.e("ComprobacionesBillRepository", "Excepción no controlada: ${e}")
            throw BillException.UnknownError(e.message)
        }
    }


    override fun getBills(): Flow<BaseResult<List<Bill>>> = flow<BaseResult<List<Bill>>> {
        syncBills()    // Usamos emitAll con map para mantener el flujo de Room vivo sin errores de cast
        emitAll(
            _dao.getAll().map { entities ->
                val bills: List<Bill> = entities.map { it.toModel() }
                BaseResult.Success(bills)
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(if (e is Exception) e else Exception(e.toString())))
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
        emit(BaseResult.Error(if (e is Exception) e else Exception(e.toString())))
    }.flowOn(Dispatchers.IO)
    override fun getBillById(id: Int): Flow<BaseResult<Bill>> = flow<BaseResult<Bill>> {
        _dao.getById(id).collect { entity ->
            emit(BaseResult.Success(entity.toModel()))
        }
    }.catch { e ->
        val exception = if (e is Exception) e else Exception(e.toString())
        emit(BaseResult.Error(exception))
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
