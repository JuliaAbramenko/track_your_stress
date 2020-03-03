package com.example.trackyourstress_ba.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.example.trackyourstress_ba.kotlin.HomeUtils
import com.example.trackyourstress_ba.kotlin.TokenUtils
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer : DrawerLayout
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView
    lateinit var conUtils: ConnectionUtils
    lateinit var usernameText: TextView
    lateinit var emailText: TextView
    lateinit var homeUtils: HomeUtils
    lateinit var root: LinearLayout
    lateinit var notifications: ArrayList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()
        homeUtils = HomeUtils()
        homeUtils.initiateTokenRefresher(this)
        val preferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        if (!preferences.contains("dailyNotification") && !preferences.contains("weeklyNotification") && !preferences.contains(
                "monthlyNotification"
            )
        ) {
            homeUtils.initiateNotificationSettings(this)
        } else {
            notifications = homeUtils.getNotificationSettings(this)
            homeUtils.initiateScheduling(this, notifications)
        }



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

        val profileFragment = ProfileFragment()
        val notificationsFragment = NotificationsFragment()
        val activitiesFragment = ActivitiesFragment()
        val questionnairesFragment = QuestionnairesFragment()


        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_profile -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, profileFragment)
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
                R.id.nav_questionnaires -> {
                    val transact = supportFragmentManager.beginTransaction()
                    deleteAllViews()
                    transact.replace(R.id.fragment_container, questionnairesFragment)
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
