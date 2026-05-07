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

    /**
     * Se encarga de crear el canal para la publicación de notificaciones.
     */
    private fun createNotificationChannel() {
        // Creo el canal
        val channel = NotificationChannel(
            notificationChannelID,
            "Cuentas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de activación de cuenta"
        }

        // Creo un canal de notificaciones para el canal creado anteriormente
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Encargado de lanzar la notificación
     */
    fun showSimpleNotification(contentTitle: String, contentText: String) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.iberdorla_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.EnergyGreen))
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}