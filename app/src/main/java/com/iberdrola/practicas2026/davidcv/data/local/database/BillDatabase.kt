package com.iberdrola.practicas2026.davidcv.data.local.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import com.iberdrola.practicas2026.davidcv.data.local.converters.BillTypeConverter
import com.iberdrola.practicas2026.davidcv.data.local.converters.DateConverter
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
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
    version = 8,
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
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    INSTANCE?.let { database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            prepopulateDatabase(context, database)
                                        }
                                    }
                                }

                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
                                    INSTANCE?.let { database ->
                                        CoroutineScope(Dispatchers.IO).launch {
                                            prepopulateDatabase(context, database)
                                        }
                                    }
                                }
                            },
                        ).build()
                INSTANCE = instance
                instance
            }

        private suspend fun prepopulateDatabase(context: Context, database: BillDatabase) {
            Log.d("Comprobaciones", "Poblando base de datos desde el nuevo formato JSON...")
            try {
                val dao = database.billDao()

                val jsonString = context.assets.open("BillJSON.json").bufferedReader().use { it.readText() }

                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { json, _, _ ->
                        val dateStr = json.asString.replace("Z", "")
                        LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    })
                    .create()

                val type = object : TypeToken<List<BillEntity>>() {}.type
                val bills: List<BillEntity> = gson.fromJson(jsonString, type)

                //bills.forEach { dao.insert(it) }

            } catch (e: Exception) {
                Log.e("Comprobaciones", "Error al poblar base de datos: ${e.message}")
            }
        }
    }
}