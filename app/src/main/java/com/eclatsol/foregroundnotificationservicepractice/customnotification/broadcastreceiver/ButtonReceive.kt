package com.eclatsol.foregroundnotificationservicepractice.customnotification.broadcastreceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ButtonReceive : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "BUTTONACTION" -> {
                val msg = intent.extras?.getString("msg") ?: "Not Found"
                when (msg) {
                    "btn1" -> {
                        closeNotificationControBar(context)
                        activityIntent(
                            context,
                            context?.packageName.toString(),
                            "com.eclatsol.foregroundnotificationservicepractice.customnotification.ui.FirstActivity"
                        )
                    }

                    "btn2" -> {
                        closeNotificationControBar(context)
                        activityIntent(
                            context,
                            context?.packageName.toString(),
                            "com.eclatsol.foregroundnotificationservicepractice.customnotification.ui.SecondActivity"
                        )
                    }

                    "btn3" -> {
                        closeNotificationControBar(context)
                        activityIntent(
                            context,
                            context?.packageName.toString(),
                            "com.eclatsol.foregroundnotificationservicepractice.customnotification.ui.ThirdActivity"
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun closeNotificationControBar(context: Context?) {
        try {
            val statusBarService = context?.getSystemService("statusbar")
            val statusBarManager = Class.forName("android.app.StatusBarManager")
            val collapsePanelsMethod = statusBarManager.getMethod("collapsePanels")
            collapsePanelsMethod.invoke(statusBarService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun activityIntent(context: Context?, packageName: String, activity: String) {
        val intent = Intent()
        intent.setClassName(packageName, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intent)
    }
}