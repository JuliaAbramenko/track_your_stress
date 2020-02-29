package com.example.trackyourstress_ba.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.trackyourstress_ba.MainActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.fragments.NotificationsFragment
import com.example.trackyourstress_ba.fragments.ProfileFragment
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.fragments.ActivitiesFragment
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.example.trackyourstress_ba.kotlin.HomeUtils
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer : DrawerLayout
    lateinit var drawer_toggle : ActionBarDrawerToggle
    lateinit var nav_view : NavigationView
    lateinit var connection_utils: ConnectionUtils
    lateinit var username_text: TextView
    lateinit var email_text: TextView
    lateinit var refresh_handler: Handler
    lateinit var notification_manager: NotificationManager
    lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_daily_notifications)
            val descriptionText = getString(R.string.channel_daily_notifications) + "testing"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel =
                NotificationChannel(R.string.channel_id_daily.toString(), name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

    }

    override fun onStart() {
        super.onStart()
        root = findViewById(R.id.homeRoot)
        connection_utils = ConnectionUtils()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        drawer_toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(drawer_toggle)
        drawer_toggle.syncState()
        nav_view = findViewById(R.id.nav_view)
        nav_view.setItemIconTintList(null)
        refresh_handler = Handler()
        // refreshes after 60 * 60 * 1000 = 3 600 000 milli seconds (1 hour)
        refresh_handler.postDelayed(
            { connection_utils.refreshToken(this) },
            3600000
        )
        val homeUtils = HomeUtils()

        val headerLayout: View = nav_view.getHeaderView(0)
        username_text = headerLayout.findViewById(R.id.username_drawer)
        email_text = headerLayout.findViewById(R.id.email_drawer)

        username_text.text = GlobalVariables.localStorage["username"]
        email_text.text = GlobalVariables.localStorage["current_email"]

        val profile_frag = ProfileFragment()
        val notifications_fragment = NotificationsFragment()
        val activitiesFragment = ActivitiesFragment()
        val questionnaires_fragment = QuestionnairesFragment()


//TODO delete mainviews?

        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_profile -> {
                    val transact = supportFragmentManager.beginTransaction()
                    //transact.addToBackStack(null)
                    transact.replace(R.id.fragment_container, profile_frag)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_notifications -> {
                    val transact = supportFragmentManager.beginTransaction()
                    //transact.addToBackStack(null)
                    transact.replace(R.id.fragment_container, notifications_fragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }
                R.id.nav_questionnaires -> {
                    val transact = supportFragmentManager.beginTransaction()
                    //transact.addToBackStack(null)
                    transact.replace(R.id.fragment_container, questionnaires_fragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }
                R.id.nav_activities -> {
                    val transact = supportFragmentManager.beginTransaction()
                    //transact.addToBackStack(null)
                    transact.replace(R.id.fragment_container, activitiesFragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                R.id.nav_logout -> {
                    connection_utils.logoutUser(this)
                    drawer.closeDrawers()
                    val intent = Intent(this@HomeActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> true
            }
        }

    }

    override fun onPause() {
        super.onPause()
        refresh_handler = Handler()
        // refreshes after 60 * 60 * 1000 = 3 600 000 milli seconds (1 hour)
        refresh_handler.postDelayed(
            { connection_utils.refreshToken(this) },
            3600000
        )
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        else super.onBackPressed()
    }

    fun on_new_token_received(response: JSONObject) {
        val new_token =
            response.getJSONObject("data").getJSONObject("attributes").getString("token")
        GlobalVariables.localStorage["token"] = new_token
    }

    private fun deleteActivities() {
        root.removeAllViews()
    }
}
