package com.eclatsol.foregroundnotificationservicepractice.customnotification.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.eclatsol.foregroundnotificationservicepractice.R
import com.eclatsol.foregroundnotificationservicepractice.customnotification.broadcastreceiver.ButtonReceive

class CustomForegroundService : Service() {
    val notificationChannel = "custom"
    val serviceId = 501
    lateinit var notification: Notification
    lateinit var notificationManager: NotificationManager
    var requestCount = 0
    var handler = Handler(Looper.getMainLooper())
    var count = 0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val toastRunnable = object : Runnable{
            override fun run() {
                Toast.makeText(this@CustomForegroundService, "$count", Toast.LENGTH_SHORT).show()
                count++
                handler.postDelayed(this,1000)
            }
        }
        handler.postDelayed(toastRunnable,1000)
        startForeground(serviceId,notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannel,
                "CustomForeground",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun createNotification() {
        val remoteView = RemoteViews(packageName, R.layout.custom_notification)
        remoteView.setTextViewText(R.id.text1, "Title")
        remoteView.setTextViewText(R.id.text2, "Sub")
        remoteView.setOnClickPendingIntent(R.id.btn1, getPendingIntent("btn1"))
        remoteView.setOnClickPendingIntent(R.id.btn2, getPendingIntent("btn2"))
        remoteView.setOnClickPendingIntent(R.id.btn3, getPendingIntent("btn3"))
        val build = NotificationCompat.Builder(this, notificationChannel).setSmallIcon(
            R.drawable.ic_launcher_background
        ).setContent(remoteView)
        notification = build.build()
    }

    private fun getPendingIntent(msg: Any): PendingIntent {
        val intent = Intent(this, ButtonReceive::class.java).apply {
            action = "BUTTONACTION"
            putExtra("msg", "$msg")
            requestCount++
        }
        return PendingIntent.getBroadcast(
            this,
            requestCount,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


}