package com.example.medicineremainder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        val medicineName =
            intent.getStringExtra("MEDICINE_NAME")
                ?: "Medicine Reminder"

        val channelId = "MEDICINE_CHANNEL"
        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Medicine Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(
                    android.R.drawable.ic_dialog_info
                )
                .setContentTitle("Medicine Reminder")
                .setContentText(
                    "Time to take: $medicineName"
                )
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }
}
