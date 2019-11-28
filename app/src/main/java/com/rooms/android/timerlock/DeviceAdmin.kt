package com.rooms.android.timerlock

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DeviceAdmin : DeviceAdminReceiver() {

    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)

        Toast.makeText(context, "onEnabled", Toast.LENGTH_LONG).show()
    }

    override fun onDisabled(context: Context?, intent: Intent?) {
        super.onDisabled(context, intent)

        Toast.makeText(context, "onDisabled", Toast.LENGTH_LONG).show()
    }
}