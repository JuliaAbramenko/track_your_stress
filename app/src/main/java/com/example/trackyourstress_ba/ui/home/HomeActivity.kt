package com.example.trackyourstress_ba.ui.home

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
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
import com.example.trackyourstress_ba.fragments.*
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.HomeUtils
//import com.example.trackyourstress_ba.kotlin.TokenReceiver
//import com.example.trackyourstress_ba.kotlin.TokenUtils
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
    lateinit var notifications: BooleanArray
    //lateinit var tokenReceiver: TokenReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeUtils = HomeUtils()
        //homeUtils.initiateTokenRefresher(this)
    }

    override fun onStart() {
        super.onStart()
        val preferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )
        if (!preferences.contains("dailyNotification") && !preferences.contains("weeklyNotification") && !preferences.contains(
                "monthlyNotification"
            )
        ) {
            homeUtils.initiateNotificationSettings(this)
        }
        notifications = homeUtils.getNotificationSettings(this)
        homeUtils.initiateScheduling(this, notifications)
        //tokenReceiver = TokenReceiver()

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
