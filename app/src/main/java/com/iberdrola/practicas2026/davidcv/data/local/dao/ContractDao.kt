package com.iberdrola.practicas2026.davidcv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {

    @Query("SELECT * FROM contracts")
    fun getAll(): Flow<List<ContractEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contracts: List<ContractEntity>)

    @Query("DELETE FROM contracts")
    suspend fun deleteAll()
}
