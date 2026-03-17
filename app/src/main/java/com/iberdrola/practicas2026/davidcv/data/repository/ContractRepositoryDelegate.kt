package com.iberdrola.practicas2026.davidcv.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
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
 * ContractRepositoryDelegate
 * Repositorio para la gestión de contratos siguiendo el patrón Single Source of Truth.
 */
@Singleton
class ContractRepositoryDelegate @Inject constructor(
    private val _apiService: ApiService,
    private val _dao: ContractDao,
    private val _gson: Gson,
    @ApplicationContext private val _context: Context
) : ContractRepositoryInterface {

    private suspend fun syncContracts() {
        try {
            if (DataSourceConfig.useNetwork) {
                Log.d("ContractRepository", "Sincronizando contratos desde RED...")
                val response = _apiService.getContracts()
                if (response.isSuccessful) {
                    response.body()?.let { contracts ->
                        if (contracts.isNotEmpty()) {
                            _dao.deleteAll()
                            _dao.insertAll(contracts)
                        }
                    }
                }
            } else {
                Log.d("ContractRepository", "Sincronizando contratos desde MOCK LOCAL...")
                val jsonString = try {
                    _context.assets.open("ContractJSON.json").bufferedReader().use { it.readText() }
                } catch (e: Exception) {
                    null
                }
                
                jsonString?.let {
                    val type = object : TypeToken<List<ContractEntity>>() {}.type
                    val contractsToInsert: List<ContractEntity> = _gson.fromJson(it, type)
                    if (contractsToInsert.isNotEmpty()) {
                        _dao.insertAll(contractsToInsert)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("ContractRepository", "Error sincronizando contratos: ${e.message}")
        }
    }

    override fun getContracts(): Flow<BaseResult<List<Contract>>> = flow {
        syncContracts()
        emitAll(
            _dao.getAll().map { entities ->
                BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Contract>>
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(if (e is Exception) e else Exception(e)))
    }.flowOn(Dispatchers.IO)
}
