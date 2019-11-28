package com.rooms.android.timerlock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log

class LockActivity : BaseActivity() {

    val TAG = LockActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lock)

//        Handler().postDelayed(Runnable {
//
//            var deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//            var compName = ComponentName(this, DeviceAdmin::class.java)
//            val active = deviceManager?.isAdminActive(compName) ?: false
//
//            Log.d(TAG, "DeviceAdmin active : + " + active)
//
//            if (active) {
//                Log.d(TAG, "lockNow")
//                deviceManager?.lockNow()
//            }
//
//        }, 2000)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            startForegroundService(Intent(this, AlarmSoundService::class.java))
        }else{
            startService(Intent(this, AlarmSoundService::class.java))
        }

        finish()
    }
}