package com.iberdrola.practicas2026.davidcv.ui.helper


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import kotlin.random.Random

class NotificationHandler(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java)

    private val notificationChannelID = "ibchanel_id"

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            notificationChannelID,
            "Cuentas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de activación de cuenta"
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun showSimpleNotification(contentTitle: String, contentText: String) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.iberdrola_logo_notificaciones)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.EnergyGreen))
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}