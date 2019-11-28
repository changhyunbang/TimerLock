package com.rooms.android.timerlock

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

class SharedPref (context: Context) {

    val PREFS_FILENAME = "com.rooms.android.timerlock.prefs"
    val ALARM_TIME = "alarm_time"
    val ALARM_STATUS = "alarm_status"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    var alarmTime: Long
        get() = prefs.getLong(ALARM_TIME, -1)
        set(value) = prefs.edit().putLong(ALARM_TIME, value).apply()

    var alarmStatus: Boolean
        get() = prefs.getBoolean(ALARM_STATUS, false)
        set(value) = prefs.edit().putBoolean(ALARM_STATUS, value).apply()
}