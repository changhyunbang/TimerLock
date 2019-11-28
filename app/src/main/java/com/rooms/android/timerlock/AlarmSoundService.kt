package com.rooms.android.timerlock

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import android.media.MediaPlayer
import java.lang.reflect.Array.getLength
import android.content.res.AssetFileDescriptor
import android.os.Handler
import java.io.IOException
import android.support.v4.app.NotificationCompat
import android.app.Service.START_NOT_STICKY
import android.graphics.*
import android.os.Build
import android.support.v4.content.ContextCompat.getSystemService
import android.view.*
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.view.Gravity
import android.R.attr.gravity
import android.app.*
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.view.WindowManager
import android.view.LayoutInflater



class AlarmSoundService : Service() {

    val TAG = AlarmSoundService::class.java.simpleName
    var mView: ViewGroup ?= null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        Log.d(TAG, "onBind")
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate()")
        Log.d(TAG, "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT)

        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_service";
            val CHANNEL_NAME = "My Background Service";

            var channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_NONE);
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel);

            var notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();

            startForeground(101, notification);
        }

//         val NOTIFICATION_ID: Int = (System.currentTimeMillis()%10000) as Int;
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(NOTIFICATION_ID, Notification.Builder(this).build());
//        }

        val inflate = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        var type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        val params = WindowManager.LayoutParams(
         /*ViewGroup.LayoutParams.MATCH_PARENT*/300,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.CENTER
        mView = inflate.inflate(R.layout.view_in_service, null) as ViewGroup?
        val textView = mView?.findViewById(R.id.textView) as TextView
        val bt = mView?.findViewById(R.id.bt) as ImageButton
        bt.setOnClickListener(View.OnClickListener {
//         fun onClick(v:View) {
//        bt.setImageResource(R.mipmap.ic_launcher_round)
//             textView.text = "on click!!"
        })
        wm.addView(mView, params)
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy()")

        if(mView != null)
        {
            (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(mView);
            mView = null;
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG, "onStartCommand()")

        Toast.makeText(this, "Alarm Sound Start", Toast.LENGTH_LONG).show()

        val player = playWavResource(this, R.raw.fxarms)

        player.setOnPreparedListener {
            Log.d(TAG, "Alarm OnPrepared")
        }
        player.setOnCompletionListener {
            Log.d(TAG, "Alarm OnCompletion")

            screenLockDelay(this, 0)
            stopSelf();
        }

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
//
//                stopSelf();
//            }
//        }, 5000)

//        var runnable = Runnable {
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
//        }
//
//        Handler().postDelayed(runnable, 5000)

        return START_NOT_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }

    fun playWavResource(context: Context, resWav: Int) : MediaPlayer{
        val afd = context.getResources().openRawResourceFd(resWav)

        Log.d(TAG, afd.toString())
        val fileDescriptor = afd.getFileDescriptor()
        val player = MediaPlayer()
        try {
            player.setDataSource(
                fileDescriptor, afd.getStartOffset(),
                afd.length
            )
            player.isLooping = false
            player.prepare()
            player.start()
        } catch (ex: IOException) {
            Log.e(TAG, ex.toString());
        }

        return player
    }

    fun screenLockDelay(context: Context, delayMillis: Long) {
        Handler().postDelayed(Runnable {

            var deviceManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            var compName = ComponentName(context, DeviceAdmin::class.java)
            val active = deviceManager?.isAdminActive(compName) ?: false

            Log.d(TAG, "DeviceAdmin active : + " + active)

            if (active) {
                Log.d(TAG, "lockNow")
                deviceManager?.lockNow()
            }

        }, delayMillis)
    }
}