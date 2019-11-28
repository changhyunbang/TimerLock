package com.rooms.android.timerlock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View

class DeviceManager {

    var deviceManager: DevicePolicyManager? = null
    var compName: ComponentName? = null
    var context: Context? = null

    fun DeviceManager(ctx: Context, cls: Class<*>) {
        context = ctx;
        deviceManager = context?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(context, cls)
    }

    fun isActive() : Boolean {

        val active = deviceManager?.isAdminActive(compName) ?: false

        return false;
    }
}