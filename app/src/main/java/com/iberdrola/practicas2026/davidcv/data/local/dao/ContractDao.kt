package com.iberdrola.practicas2026.davidcv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {

    @Query("SELECT * FROM contracts")
    fun getAll(): Flow<List<ContractEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contracts: List<ContractEntity>)

    @Query("UPDATE contracts SET email = :email WHERE id = :id")
    suspend fun updateEmail(id: Int, email: String)

    @Query("UPDATE contracts SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("UPDATE contracts SET email = :email, status = :status WHERE id = :id")
    suspend fun updateEmailAndStatus(id: Int, email: String, status: String)

    @Query("DELETE FROM contracts")
    suspend fun deleteAll()
}
