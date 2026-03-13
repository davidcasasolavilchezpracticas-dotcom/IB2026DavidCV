package com.iberdrola.practicas2026.davidcv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import kotlinx.coroutines.flow.Flow

/**
 * BillDao
 * Interfaz para acceder a la base de datos
 */
@Dao
interface BillDao {
    @Query("SELECT * FROM bills")
    fun getAll(): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE type = :type ORDER BY endDate DESC")
    fun getAllBillsByType(type: BillType): Flow<List<BillEntity>>

    @Query("SELECT * FROM bills WHERE id = :id")
    fun getById(id: Int): Flow<BillEntity>

    @Delete
    suspend fun delete(bill: BillEntity)

    @Update
    suspend fun update(bill: BillEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: BillEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bills: List<BillEntity>)

    @Query("DELETE FROM bills")
    suspend fun deleteAll()
}
