package com.example.scandoc.data.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import com.example.scandoc.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class ProcessingNotificationManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val notificationManager: NotificationManager
            by lazy { context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

        fun createNotificationChannel() {
            if (SDK_INT < O) return

            val channel =
                NotificationChannel(
                    context.getString(R.string.processing_notification_channel_id),
                    context.getString(R.string.processing_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    enableLights(false)
                    enableVibration(false)
                }

            notificationManager.createNotificationChannel(channel)
        }

        fun getNotification(
            title: String,
            progress: Boolean = true,
        ): Notification =
            NotificationCompat
                .Builder(context, context.getString(R.string.processing_notification_channel_id))
                .apply {
                    setSmallIcon(R.drawable.ic_launcher_foreground)
                    setContentTitle(title)
                    setContentText(getNotificationContent(progress))
                    setAutoCancel(true)
                    if (progress) {
                        setOngoing(true)
                        setProgress(STUB, STUB, true)
                    }
                }.build()

        fun sendSuccessNotification(
            title: String,
            workName: String,
        ) {
            (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                SUCCESS_NOTIFICATION_ID + UUID.fromString(workName).hashCode(),
                getNotification(title, false),
            )
        }

        fun sendFailureNotification(
            title: String,
            workName: String,
        ) {
            (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(
                SUCCESS_NOTIFICATION_ID + UUID.fromString(workName).hashCode(),
                getNotification(title, false),
            )
        }

        private fun getNotificationContent(progress: Boolean) =
            context.getString(
                if (progress) {
                    R.string.processing_notification_progress
                } else {
                    R.string.processing_notification_finished
                },
            )

        companion object {
            const val PROGRESS_NOTIFICATION_ID = 12345
            private const val SUCCESS_NOTIFICATION_ID = 54321
            private const val STUB = 0
        }
    }
