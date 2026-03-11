package com.iberdrola.practicas2026.davidcv.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iberdrola.practicas2026.davidcv.data.local.converters.BillTypeConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.DateConverter
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(
    version = 6,
    entities = [BillEntity::class],
    exportSchema = false,
)
@TypeConverters(value = [DateConverter::class, BillTypeConverter::class])
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDao(): BillDao

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
                        .fallbackToDestructiveMigration()
                        .addCallback(
                            object : Callback() {
                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
                                    // Comprobamos si está vacía cada vez que se abre
                                    // por si el onCreate falló
                                    INSTANCE?.let { database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val dao = database.billDao()
                                            if (dao.getAll().isEmpty()) {
                                                prepopulateDatabase(database)
                                            }
                                        }
                                    }
                                }
                            },
                        ).build()
                INSTANCE = instance
                instance
            }

        private suspend fun prepopulateDatabase(database: BillDatabase) {
            Log.d("Comprobaciones", "Poblando base de datos...")
            val dao = database.billDao()
            val now = LocalDateTime.now()

            val bills = listOf(
                BillEntity(id = 1, type = BillType.GAS, value = 20, startDate = now.minusMonths(1), endDate = now, paymentStatus = true),
                BillEntity(id = 2, type = BillType.GAS, value = 60, startDate = now.minusMonths(2), endDate = now.minusMonths(1), paymentStatus = false),
                BillEntity(id = 3, type = BillType.GAS, value = 20, startDate = now.minusMonths(3), endDate = now.minusMonths(2), paymentStatus = true),
                BillEntity(id = 4, type = BillType.LIGHT, value = 60, startDate = now.minusMonths(1), endDate = now, paymentStatus = false),
                BillEntity(id = 5, type = BillType.LIGHT, value = 150, startDate = now.minusYears(1).minusMonths(1), endDate = now.minusYears(1), paymentStatus = true),
                BillEntity(id = 6, type = BillType.LIGHT, value = 150, startDate = now.minusMonths(2), endDate = now.minusMonths(1), paymentStatus = true),
                BillEntity(id = 7, type = BillType.LIGHT, value = 150, startDate = now.minusYears(1).minusMonths(2), endDate = now.minusYears(1).minusMonths(1), paymentStatus = true),
                BillEntity(id = 8, type = BillType.LIGHT, value = 150, startDate = now.minusYears(2).minusMonths(1), endDate = now.minusYears(2).minusMonths(2), paymentStatus = true)
            )

            bills.forEach { dao.insert(it) }
            Log.d("Comprobaciones", "¡Base de datos poblada con éxito!")
        }
    }
}
