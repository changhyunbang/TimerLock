package com.rooms.android.timerlock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.ViewGroup

internal class HUDView(context: Context, var kangoo: Bitmap) : ViewGroup(context) {

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)

        canvas?.drawColor(Color.BLACK)

        canvas?.drawBitmap(kangoo, 0.0f, 0.0f, null)
    }


    override fun onLayout(arg0: Boolean, arg1: Int, arg2: Int, arg3: Int, arg4: Int) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //return super.onTouchEvent(event);
        // Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();

        return true
    }
}