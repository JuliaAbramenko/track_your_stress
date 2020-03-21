package com.example.trackyourstress_ba.ui.home

//import com.example.trackyourstress_ba.kotlin.TokenReceiver
//import com.example.trackyourstress_ba.kotlin.TokenUtils
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.fragments.*
import com.example.trackyourstress_ba.kotlin.*
import com.google.android.material.navigation.NavigationView
import java.util.*


class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer : DrawerLayout
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView
    lateinit var conUtils: ConnectionUtils
    lateinit var usernameText: TextView
    lateinit var emailText: TextView
    //lateinit var homeUtils: HomeUtils
    lateinit var root: LinearLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var notificationManagement: NotificationManagement
    //lateinit var notifications: BooleanArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun onStart() {
        super.onStart()
        notificationManagement = NotificationManagement(this)
        sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        if (!sharedPreferences.contains("dailyNotification") &&
            !sharedPreferences.contains("weeklyNotification") &&
            !sharedPreferences.contains("monthlyNotification")
        ) {
            notificationManagement.initiateNotificationSettings(this)
        } else {
            notificationManagement.initiateScheduling(this)
        }
        startTokenRefresher()
        test()

        root = findViewById(R.id.homeRoot)
        conUtils = ConnectionUtils()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null


        val headerLayout: View = navView.getHeaderView(0)
        usernameText = headerLayout.findViewById(R.id.username_drawer)
        emailText = headerLayout.findViewById(R.id.email_drawer)
        val sharedPrefs = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        val userName = sharedPrefs.getString("userName", null)
        usernameText.text = userName
        val userEmail = sharedPrefs.getString("userEmail", null)
        emailText.text = userEmail

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val notificationsFragment = NotificationsFragment()
        val activitiesFragment = ActivitiesFragment()
        val questionnairesFragment = QuestionnairesFragment()
        val impressumFragment = ImpressumFragment()
        val aboutFragment = AboutFragment()


        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, homeFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_profile -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, profileFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_activities -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, activitiesFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_questionnaires -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, questionnairesFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_notifications -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, notificationsFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }
                R.id.nav_about_us -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, aboutFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_impressum -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, impressumFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }
                R.id.nav_logout -> {
                    conUtils.logoutUser(this)
                    drawer.closeDrawers()
                    returnToLogin()
                    true
                }

                else -> true
            }
        }
    }

    private fun test() {
        val builder = NotificationCompat.Builder(this, "999")
            .setSmallIcon(R.drawable.ic_notifications_24dp)
            .setContentTitle("TrackYourStress")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "trackyourstress"
            val descriptionText = "strange channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("999", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }

    }

    /*private fun startAlarm(isRepeat: Boolean) {
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        /*val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 17)
        calendar.set(Calendar.MINUTE, 24)*/

        val intent = Intent(this, NotificationPublisher::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 999, intent, 0)
        Log.w("notification manager", "notifier active")

        if (!isRepeat)
            manager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                pendingIntent
            )
        else
            manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                60L*1000L*2,
                pendingIntent
            )
    }*/

    private fun startTokenRefresher() {
        val intent = Intent(this, TokenReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this.applicationContext, 111, intent, FLAG_CANCEL_CURRENT // FLAG_UPDATE_CURRENT
        )
        Log.w("token refresher", "token refresher active")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            //alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
    }


    private fun returnToLogin() {
        val intent = Intent(this@HomeActivity, MainActivity::class.java)
        startActivity(intent)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        else super.onBackPressed()
    }


    private fun deleteAllViews() {
        root.removeAllViews()
    }

    fun notify500() {
        Toast.makeText(
            applicationContext,
            "Fehler bei Logout",
            Toast.LENGTH_LONG
        ).show()
    }
}
