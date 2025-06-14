package com.example.currencies.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Create and show the notification when broadcast is received
        val notificationService = NotificationService(context)
        notificationService.showDailyReminder()
    }
}
