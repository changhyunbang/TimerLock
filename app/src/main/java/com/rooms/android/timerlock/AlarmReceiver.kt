package com.rooms.android.timerlock

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "onReceive Context : " + context + " Intent : " + intent?.action.toString())

        when (intent?.action.toString()) {
            "com.test.alarmtestous.ALARM_START" -> {
                val prefs = SharedPref(context!!)
                prefs!!.alarmStatus = false

//                var deviceManager = context?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//                var compName = ComponentName(context, DeviceAdmin::class.java)
//                val active = deviceManager?.isAdminActive(compName) ?: false
//
//                Log.d(TAG, "DeviceAdmin active : + " + active)
//
//                if (active) {
//                    Log.d(TAG, "lockNow")
//                    deviceManager?.lockNow()
//                }

                var intent = Intent(context!!, LockActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context?.startActivity(intent)

//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//                    context?.startForegroundService(Intent(context, AlarmSoundService::class.java))
//                }else{
//                    context?.startService(Intent(context, AlarmSoundService::class.java))
//                }
            }
        }
    }
}