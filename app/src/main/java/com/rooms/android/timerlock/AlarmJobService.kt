package com.rooms.android.timerlock

import android.app.admin.DevicePolicyManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.os.AsyncTask
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log

class AlarmJobService : JobService {

    var context: Context ?= null
    var currentTask:PollTask ?= null

    companion object {
        val TAG = AlarmJobService::class.java.simpleName
    }

    constructor() {
        context = this
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        currentTask = PollTask()
        currentTask?.execute(params)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        currentTask?.cancel(true)
        return true
    }

//    class PollTask : AsyncTask<JobParameters, Void, Void> {
//
//        constructor()
//
//        override fun doInBackground(vararg params: JobParameters?) {
//            var jobParams = params[0]
//        }
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//        }
//
//        override fun onPostExecute(result: Void?) {
//            super.onPostExecute(result)
//        }
//    }

//    class doAsync(val handler: () -> Unit) : AsyncTask<JobParameters, Void, Void>() {
//        init {
//            execute()
//        }
//
//        override fun doInBackground(vararg params: JobParameters?): Void? {
//            handler()
//            return null
//        }
//    }

    class PollTask : AsyncTask<JobParameters, Any, Any>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: JobParameters?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)
        }
    }
}