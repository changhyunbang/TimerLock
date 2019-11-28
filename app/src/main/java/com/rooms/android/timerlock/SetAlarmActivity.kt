package com.rooms.android.timerlock

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import android.app.TimePickerDialog
import android.app.admin.DevicePolicyManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.provider.Settings.canDrawOverlays
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager


class SetAlarmActivity : BaseActivity() {

    val TAG = SetAlarmActivity::class.java.simpleName

    val reqCode = 1000
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1
    val ALARM_ACTION = "com.test.alarmtestous.ALARM_START"

    var llItem: LinearLayout ?= null
    var tvAmPm: TextView ?= null
    var tvTime: TextView ?= null
    var tvDate: TextView ?= null
    var swAlarm: Switch ?= null

    var alarmCalendar = Calendar.getInstance();

    var alarmManager: AlarmManager ?= null
    var prefs: SharedPref? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        llItem = findViewById(R.id.ll_item)
        tvAmPm = findViewById(R.id.tv_ampm)
        tvTime = findViewById(R.id.tv_time)
        tvDate = findViewById(R.id.tv_date)
        swAlarm = findViewById(R.id.sw_alarm)

        initialize();

//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.images)
//
//        val LAYOUT_FLAG: Int
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//        } else {
//            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE
//        }
//
////        var option = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//        var params = WindowManager.LayoutParams(bitmap.width,
//            bitmap.height,
//            LAYOUT_FLAG,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//            PixelFormat.TRANSLUCENT)
//
//        params.gravity = Gravity.CENTER;
//        params.setTitle("Load Average");
//        var wm = getSystemService(Service.WINDOW_SERVICE) as WindowManager
//
//        var mView = HUDView(this, bitmap);
//
////        mView.setOnTouchListener(new OnTouchListener() {
////
////
////            @Override
////            public boolean onTouch(View arg0, MotionEvent arg1) {
////                // TODO Auto-generated method stub
////                //Log.e("kordinatlar", arg1.getX()+":"+arg1.getY()+":"+display.getHeight()+":"+kangoo.getHeight());
////                if(arg1.getX()<kangoo.getWidth() & arg1.getY()>0)
////                {
////                    Log.d("tıklandı", "touch me");
////                }
////                return false;
////            }
////        });
//
//        wm.addView(mView, params);
    }

    fun initialize() {

        prefs = SharedPref(this)
        val alarmLastTime = prefs!!.alarmTime
        val alarmStatus = prefs!!.alarmStatus
        if (alarmLastTime > 0) {
            alarmCalendar.timeInMillis = alarmLastTime
            setAlarmUi(alarmCalendar)
            swAlarm!!.setChecked(alarmStatus)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?

        swAlarm?.setOnCheckedChangeListener { buttonView, isChecked ->

            Log.d(TAG, "setOnCheckedChangeListener isChecked : " + isChecked)

            setAlarm(isChecked, alarmCalendar)
        }
    }

    fun setAlarm(onOff: Boolean, calendal: Calendar) {

        val alarmIntent = Intent(ALARM_ACTION)
        alarmIntent.setClass(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, reqCode, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (onOff) {

            Log.d(TAG, "setAlarm : " + SimpleDateFormat("yyyy년 MM월 dd일 E요일").format(calendal.time))
            Log.d(TAG, "setAlarm : " + SimpleDateFormat("hh:mm:ss a").format(calendal.time))

            alarmManager?.set(
                AlarmManager.RTC,
                calendal.timeInMillis,
                pendingIntent)

            Log.d(TAG, "Scheduled JobA")
        } else {
            alarmManager?.cancel(pendingIntent)
        }

        prefs!!.alarmStatus = onOff
    }

    fun clickTest(view: View) {
        var deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        var compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManager?.isAdminActive(compName) ?: false

        Log.d(TAG, "DeviceAdmin active : + " + active)

        if (active) {
            Log.d(TAG, "lockNow")
            deviceManager?.lockNow()
        }
    }

    fun itemClick(view: View) {
        showTime()
    }

    fun showTime() {

        val currentCalendal = Calendar.getInstance()
        currentCalendal.add(Calendar.MINUTE, 1)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            alarmCalendar.set(Calendar.MINUTE, minute)
            alarmCalendar.set(Calendar.SECOND, 0)

            Log.d(TAG, "showTime : " + SimpleDateFormat("yyyy년 MM월 dd일 E요일").format(alarmCalendar.time))
            Log.d(TAG, "showTime : " + SimpleDateFormat("hh:mm:ss a").format(alarmCalendar.time))

            prefs!!.alarmTime = alarmCalendar.timeInMillis

            setAlarmUi(alarmCalendar)

            swAlarm?.isChecked = true

        }, currentCalendal.get(Calendar.HOUR_OF_DAY), currentCalendal.get(Calendar.MINUTE), true)

        timePickerDialog.setMessage("메시지")
        timePickerDialog.show()
    }

    fun setAlarmUi(calendar: Calendar) {
        tvAmPm?.setText(SimpleDateFormat("a").format(calendar.time))
        tvTime?.setText(SimpleDateFormat("h:mm").format(calendar.time))
        tvDate?.setText(SimpleDateFormat("MM월dd일(E)").format(calendar.time))
    }
}