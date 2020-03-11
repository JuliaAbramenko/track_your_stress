package com.example.trackyourstress_ba.kotlin

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.net.ConnectivityManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.trackyourstress_ba.ui.home.HomeActivity

class TokenUtils {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private lateinit var tokenReceiver: TokenReceiver

    fun scheduleTokenRefresher(activity: HomeActivity) {
        /*val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(Intent.ACTION_DEFAULT)
        }
        LocalBroadcastManager.getInstance(activity).registerReceiver(TokenReceiver(), filter)
*/
        alarmMgr = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(activity, TokenReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(activity, 0, intent, 0)

        }
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000, //* 60 * 30, //all 30 minutes refresh
            alarmIntent
        )
    }


}