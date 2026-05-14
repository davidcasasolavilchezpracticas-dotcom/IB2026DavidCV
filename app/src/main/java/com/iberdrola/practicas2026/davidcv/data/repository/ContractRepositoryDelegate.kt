package com.iberdrola.practicas2026.davidcv.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toEntity
import com.iberdrola.practicas2026.davidcv.data.mappers.toModel
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.contract

@Singleton
class ContractRepositoryDelegate @Inject constructor(
    private val _apiService: ApiService,
    private val _dao: ContractDao,
    private val _gson: Gson,
    @ApplicationContext private val _context: Context
) : ContractRepositoryInterface {
    private val syncMutex = Mutex()

    private val MOCK_INTERNAL_FILE_NAME = "ContractJSON_Internal.json"
    private val MOCK_FILE_NAME = "ContractJSON.json"

    private suspend fun syncContracts() = syncMutex.withLock {
        try {
            val contractsToInsert = if (DataSourceConfig.useNetwork) { fetchFromNetwork() }
            else { fetchFromMock() }

            _dao.clearAndInsert(contractsToInsert)
        } catch (e: ContractException) {
            throw e
        } catch (e: Exception) {
            throw ContractException.UnknownError(e.message)
        }
    }

    private suspend fun fetchFromNetwork(): List<ContractEntity> {
        val response = try {
            _apiService.getContracts()
        } catch (e: IOException) {
            throw ContractException.ConexionFailed
        }

        if (!response.isSuccessful) {
            throw ContractException.ResponseError("Error RED: ${response.code()}")
        }

        val body = response.body() ?: throw ContractException.DataCorrupted

        return try {
            body.map { it.toModel().toEntity() }
        } catch (e: Exception) {
            throw ContractException.DataCorrupted
        }
    }

    private fun fetchFromMock(): List<ContractEntity> {
        val jsonString = try {
            _context.assets.open(MOCK_FILE_NAME).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw ContractException.ResponseError("No se pudo leer el archivo mock local")
        }

        val type = object : TypeToken<List<ContractEntity>>() {}.type

        val entitiesFromContent: List<ContractEntity> = try {
            _gson.fromJson(jsonString, type)
        } catch (e: JsonSyntaxException) {
            throw ContractException.DataCorrupted
        }

        return try {
            entitiesFromContent.map { it.toModel().toEntity() }
        } catch (e: Exception) {
            throw ContractException.DataCorrupted
        }
    }


    private fun getMockJsonContent(): String? {
        val internalFile = File(_context.filesDir, MOCK_INTERNAL_FILE_NAME)
        return if (internalFile.exists()) {
            internalFile.readText()
        } else {
            try {
                val assetContent = _context.assets.open(MOCK_FILE_NAME).bufferedReader().use { it.readText() }
                internalFile.writeText(assetContent)
                assetContent
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun saveCurrentDbToJson() {
        try {
            val currentEntities = _dao.getAll().first()
            val jsonString = _gson.toJson(currentEntities)
            val internalFile = File(_context.filesDir, MOCK_FILE_NAME)
            internalFile.writeText(jsonString)
        } catch (e: Exception) {
            Log.e("ContractRepository", "Error al guardar en JSON: ${e.message}")
        }
    }

    override fun getContracts(): Flow<BaseResult<List<Contract>>> = flow<BaseResult<List<Contract>>> {
        syncContracts()
        emitAll(
            _dao.getAll().map { entities ->
                val contracts = entities.map { it.toModel() }
                BaseResult.Success(contracts)
            }
        )
    }.catch { e ->
        emit(BaseResult.Error(e as? ContractException ?: ContractException.UnknownError(e.message)))
    }.flowOn(Dispatchers.IO)


    override suspend fun updateContractEmail(id: Int, email: String): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updateEmail(id, email)
            saveCurrentDbToJson()
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractPhone(id: Int, phone: String): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updatePhone(id, phone)
            saveCurrentDbToJson()
            BaseResult.Success(Unit)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override suspend fun updateContractEmail(id: Int, email: String, status: ContractStatus): BaseResult<Unit> = withContext(Dispatchers.IO) {
        try {
            _dao.updateEmailAndStatus(id, email, status.name)
            saveCurrentDbToJson()
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
