package com.example.trackyourstress_ba.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.fragments.*
import com.example.trackyourstress_ba.kotlin.ClearingUtils
import com.example.trackyourstress_ba.kotlin.ConnectionUtils
import com.example.trackyourstress_ba.kotlin.NotificationWorker
import com.example.trackyourstress_ba.kotlin.TokenWorker
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var conUtils: ConnectionUtils
    private lateinit var usernameText: TextView
    private lateinit var emailText: TextView
    private lateinit var root: LinearLayout
    lateinit var sharedPreferences: SharedPreferences
    private var tokenWorkerRunning = false
    private var notificationWorkerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = this.getSharedPreferences(
            this.packageName, Context.MODE_PRIVATE
        )

        if (!notificationWorkerRunning) {
            val notificationRequest =
                PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "NotificationRequest",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    notificationRequest
                )
            notificationWorkerRunning = true
        }

        if (!tokenWorkerRunning) {
            val tokenRequest =
                PeriodicWorkRequestBuilder<TokenWorker>(55, TimeUnit.MINUTES).setInitialDelay(
                    55,
                    TimeUnit.MINUTES
                ).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "TokenRequest",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    tokenRequest
                )

            tokenWorkerRunning = true
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

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val notificationsFragment = SettingsFragment()
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
                    ClearingUtils.clearSharedPreferences(this)
                    ClearingUtils.showLogout(this)
                    ClearingUtils.returnToLogin(this)
                    true
                }

                else -> true
            }
        }
    }

    /*private fun returnToLogin() {
        val intent = Intent(this@HomeActivity, StartActivity::class.java)
        startActivity(intent)
    }*/
    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(
            this,
            getString(R.string.double_click_back),
            Toast.LENGTH_SHORT
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun deleteAllViews() {
        root.removeAllViews()
    }

    fun notify500() {
        Toast.makeText(
            applicationContext,
            getString(R.string.error_logout),
            Toast.LENGTH_LONG
        ).show()
    }

}
