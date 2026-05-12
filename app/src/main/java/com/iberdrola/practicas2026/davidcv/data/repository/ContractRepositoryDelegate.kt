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
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContractRepositoryDelegate @Inject constructor(
    private val _apiService: ApiService,
    private val _dao: ContractDao,
    private val _gson: Gson,
    @ApplicationContext private val _context: Context
) : ContractRepositoryInterface {

    private val MOCK_FILE_NAME = "ContractJSON_Internal.json"

    /**
     * Obtiene el contenido del JSON desde el almacenamiento interno o assets.
     */
    private fun getMockJsonContent(): String? {
        val internalFile = File(_context.filesDir, MOCK_FILE_NAME)
        return if (internalFile.exists()) {
            internalFile.readText()
        } else {
            // Si no existe en interno, lo leemos de assets y lo inicializamos
            try {
                val assetContent = _context.assets.open("ContractJSON.json").bufferedReader().use { it.readText() }
                internalFile.writeText(assetContent)
                assetContent
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Guarda el estado actual de la DB en el archivo JSON interno.
     */
    private suspend fun saveCurrentDbToJson() {
        try {
            val currentEntities = _dao.getAll().first()
            val jsonString = _gson.toJson(currentEntities)
            val internalFile = File(_context.filesDir, MOCK_FILE_NAME)
            internalFile.writeText(jsonString)
        } catch (e: Exception) {
            Log.e("ComprobacionesContractRepository", "Error al guardar en JSON: ${e.message}")
        }
    }

    private suspend fun syncContracts() {
        try {
            if (DataSourceConfig.useNetwork) {
                val response = _apiService.getContracts()
                if (response.isSuccessful) {
                    response.body()?.let { contracts ->
                        _dao.clearAndInsert(contracts)
                        saveCurrentDbToJson()
                        return
                    }
                }
            }
        } catch (e: Exception) {
            throw ContractException.ConexionFailed
        }

        getMockJsonContent()?.let { jsonString ->
            val type = object : TypeToken<List<ContractEntity>>() {}.type
            val contracts: List<ContractEntity> = _gson.fromJson(jsonString, type)
            if (contracts.isNotEmpty()) {
                _dao.clearAndInsert(contracts)
            }
        }
    }

    override fun getContracts(): Flow<BaseResult<List<Contract>>> = flow {
        syncContracts()
        emitAll(_dao.getAll().map { entities ->
            BaseResult.Success(entities.map { it.toModel() }) as BaseResult<List<Contract>>
        })
    }.catch { e ->
        emit(BaseResult.Error(if (e is IOException) ContractException.ConexionFailed else Exception(e.message)))
    }.flowOn(Dispatchers.IO)

    override suspend fun updateContractEmail(id: Int, email: String): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updateEmail(id, email)
            saveCurrentDbToJson() // PERSISTENCIA EN EL JSON
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractPhone(id: Int, phone: String): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updatePhone(id, phone)
            saveCurrentDbToJson() // PERSISTENCIA EN EL JSON
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractEmail(id: Int, email: String, status: ContractStatus): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updateEmailAndStatus(id, email, status.name)
            saveCurrentDbToJson() // PERSISTENCIA EN EL JSON
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractStatus(id: Int, status: ContractStatus): BaseResult<Unit> = withContext(Dispatchers.IO)  {
        try {
            _dao.updateStatus(id, status.name)
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }
}
