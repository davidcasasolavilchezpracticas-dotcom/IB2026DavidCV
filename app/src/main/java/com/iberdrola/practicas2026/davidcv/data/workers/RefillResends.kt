package com.iberdrola.practicas2026.davidcv.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.iberdrola.practicas2026.davidcv.data.local.datastore.DataStoreManager
import java.time.LocalDate
import java.time.LocalDateTime

class RefillResends(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    val sharedPref = context.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
    val sharedPrefEditor = sharedPref.edit()

    override suspend fun doWork(): Result {
        try {
            val dataStoreManager = DataStoreManager(applicationContext, Gson())
            sharedPrefEditor.putLong("start_time", System.currentTimeMillis()).apply()
            dataStoreManager.restartTrys()
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.success()
    }
}