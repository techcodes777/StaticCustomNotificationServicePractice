package com.eclatsol.foregroundnotificationservicepractice

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eclatsol.foregroundnotificationservicepractice.foreground.ForeGroundService

class MainActivity : AppCompatActivity() {

    val permissionCode = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSendNotification).setOnClickListener {
            checkNotificationService()
        }
    }

    private fun checkNotificationService() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.FOREGROUND_SERVICE,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),
                permissionCode
            )
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, ForeGroundService::class.java))
            } else {
                startService(Intent(this, ForeGroundService::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(Intent(this, ForeGroundService::class.java))
                    } else {
                        startService(Intent(this, ForeGroundService::class.java))
                    }
                }
            }
        }
    }
}