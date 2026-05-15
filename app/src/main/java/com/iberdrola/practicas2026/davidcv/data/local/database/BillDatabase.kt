package com.iberdrola.practicas2026.davidcv.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.converters.BillTypeConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.ContractStatusConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.ContractTypeConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.DateConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.PaymentStatusConverter
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.dao.ContractDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * BillDatabase
 * Modelo de datos para la base de datos
 */
@Database(
    version = 16,
    entities = [BillEntity::class, ContractEntity::class],
    exportSchema = false,
)
@TypeConverters(
    value = [
        DateConverter::class,
        BillTypeConverter::class,
        PaymentStatusConverter::class,
        ContractTypeConverter::class,
        ContractStatusConverter::class
    ]
)
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao
    abstract fun contractDao(): ContractDao

    companion object {
        @Volatile
        private var INSTANCE: BillDatabase? = null

        fun getDatabase(context: Context): BillDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            BillDatabase::class.java,
                            "final_bills_database.db",
                        )
                        .fallbackToDestructiveMigration(false)
                        .addCallback(
                            object : Callback() {

                            },
                        ).build()
                INSTANCE = instance
                instance
            }
    }
}
