package com.iberdrola.practicas2026.davidcv.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.BillType

/**
 * BillDao
 * Interfaz para acceder a la base de datos
 */
@Dao
interface BillDao {
    @Query("SELECT * FROM bills")
    suspend fun getAll(): List<BillEntity>

    @Query("SELECT * FROM bills WHERE type = :type ORDER BY endDate DESC")
    suspend fun getAllBillsByType(type: BillType): List<BillEntity>

    @Query("SELECT * FROM bills WHERE id = :id")
    suspend fun getById(id: Int): BillEntity

    @Delete
    suspend fun delete(bill: BillEntity)

    @Update
    suspend fun update(bill: BillEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: BillEntity)
}
