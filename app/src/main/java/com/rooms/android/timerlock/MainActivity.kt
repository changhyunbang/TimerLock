package com.rooms.android.timerlock

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.widget.Button
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.provider.Settings.canDrawOverlays
import android.os.Build
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    val RESULT_ENABLE = 1
    val OVERLAY_PERMISSION_REQUEST_CODE = 2

    var btnEnable: Button? = null
    var btnLock: Button? = null

    var deviceManager: DevicePolicyManager? = null
    var compName: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnEnable = findViewById(R.id. btnEnable )
        btnLock = findViewById(R.id. btnLock )

        deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManager?.isAdminActive(compName) ?: false

        if (active) {
            startSetAlarmActivity()
        } else {
            btnEnable?.setText( "Enable" ) ;
            btnLock?.setVisibility(View. GONE ) ;
        }
    }

    fun enablePhone(view: View) {

        val active = deviceManager?.isAdminActive(compName) ?: false

        if (active) {
            deviceManager?.removeActiveAdmin(compName)
            btnEnable?.setText( "Enable" ) ;
            btnLock?.setVisibility(View. GONE ) ;
        } else {
            var intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
            startActivityForResult(intent , RESULT_ENABLE ) ;
        }
    }

    fun lockPhone(view: View) {
        deviceManager?.lockNow() ;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RESULT_ENABLE -> {
                if (resultCode == Activity.RESULT_OK ) {
//                    btnEnable?.setText( "Disable" ) ;
//                    btnLock?.setVisibility(View. VISIBLE ) ;
                    checkPermission()
                } else {
                    Toast. makeText (getApplicationContext() , "Failed!" ,
                        Toast. LENGTH_SHORT ).show() ;
                }
            }
            OVERLAY_PERMISSION_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK ) {
//                    btnEnable?.setText( "Disable" ) ;
//                    btnLock?.setVisibility(View. VISIBLE ) ;
                    startSetAlarmActivity()
                } else {
                    Toast. makeText (getApplicationContext() , "Failed!" ,
                        Toast. LENGTH_SHORT ).show() ;
                }
            }
        }
    }

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                startSetAlarmActivity();
            }
        } else {
            startSetAlarmActivity();
        }
    }

    fun startSetAlarmActivity() {
        startActivity(Intent(this, SetAlarmActivity::class.java))
        finish();
    }
}
