package com.pdm.pdmsecurity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class MyNotificationManager(context: Context) {
    private var _context: Context = context
    fun SendNotification(text: String, level: Int) {

        try {
            val mNotificationManager =
                ContextCompat.getSystemService(
                    _context,
                    android.app.NotificationManager::class.java
                ) ?: return
            if (level == 4) {
                return
                //remove notification
            }
            val alrtUri =
                Uri.parse("android.resource://" + _context.applicationContext.getPackageName() + "/" + R.raw.alarm)
            val dangerUri =
                Uri.parse("android.resource://" + _context.applicationContext.getPackageName() + "/" + R.raw.danger)
            val notification: Notification
            val channelId = "pdm.SecuritySocket"

            val notificationIntent = Intent(_context, MainActivity::class.java)
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            var intent = PendingIntent.getActivity(
                _context, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                val channel = NotificationChannel(
                    channelId,
                    "pdm socket",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                channel.setSound(
                    dangerUri,
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                )
                mNotificationManager.createNotificationChannel(channel)
                val notificationBuilder = NotificationCompat.Builder(_context, channelId)
                notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("socket")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText(text)
                    .setSound(dangerUri)
                    .addAction(NotificationCompat.Action(0, "Open App", intent))
                    .build()
            } else {
                notification = NotificationCompat.Builder(_context)
                    .setContentTitle("socket")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText(text)
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setChannelId(channelId)
                    .setSound(dangerUri)
                    .addAction(NotificationCompat.Action(0, "Open App", intent))
                    .build()
            }
            if (level == 1)//high
            {
                val r = RingtoneManager.getRingtone(_context, alrtUri)
                r.play()
            }

            mNotificationManager.notify("socket", 1, notification)
        } catch (ex: Exception) {
            Log.e("send notification", ex.message.toString())
            //ignore
        }

    }
}