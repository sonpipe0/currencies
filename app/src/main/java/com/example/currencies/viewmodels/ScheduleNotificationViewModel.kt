package com.example.currencies.viewmodels

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.currencies.R
import com.example.currencies.notification.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ScheduleNotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    // State for notification enabled/disabled
    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // Update notification state and schedule/cancel accordingly
    fun updateNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled

        if (enabled) {
            scheduleNotification()
        } else {
            cancelNotification()
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(context, NotificationReceiver::class.java)

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get daily notification hour from resources (2:00 PM = 14:00)
        val notificationHour = context.getString(R.string.notification_daily_hour).toInt()

        // Set notification time to 2:00 PM
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, notificationHour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // If it's already past notification time, schedule for the next day
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        // Schedule the notification
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        // Update state
        _notificationsEnabled.value = true
    }

    // Function to cancel scheduled notification
    private fun cancelNotification() {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    // Function to schedule notification after a short delay (for testing)
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleTestNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(context, NotificationReceiver::class.java)

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,  // Different request code for test notification
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get test delay in milliseconds from resources (30 seconds)
        val testDelayMs = context.getString(R.string.notification_test_delay_ms).toLong()

        // Get the time for specified delay from now
        val time = System.currentTimeMillis() + testDelayMs

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
}
