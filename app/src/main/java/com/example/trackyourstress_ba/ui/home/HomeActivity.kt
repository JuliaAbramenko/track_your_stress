package com.example.trackyourstress_ba.ui.home

//import com.example.trackyourstress_ba.kotlin.TokenReceiver
//import com.example.trackyourstress_ba.kotlin.TokenUtils
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.trackyourstress_ba.StartActivity
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.fragments.*
import com.example.trackyourstress_ba.kotlin.*
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer : DrawerLayout
    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView
    lateinit var conUtils: ConnectionUtils
    lateinit var usernameText: TextView
    lateinit var emailText: TextView
    lateinit var root: LinearLayout
    lateinit var sharedPreferences: SharedPreferences
    var tokenWorkerRunning = false
    var notificationWorkerRunning = false
    //lateinit var notifications: BooleanArray

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
                    ExistingPeriodicWorkPolicy.KEEP, //or .APPEND? or cancel?
                    notificationRequest
                )
            notificationWorkerRunning = true
        }

        if (!tokenWorkerRunning) {
            val tokenRequest =
                PeriodicWorkRequestBuilder<TokenWorker>(50, TimeUnit.MINUTES).setInitialDelay(
                    50,
                    TimeUnit.MINUTES
                ).build()

            WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "TokenRequest",
                    ExistingPeriodicWorkPolicy.KEEP, //sure?
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
                    returnToLogin()
                    true
                }

                else -> true
            }
        }
    }


    private fun returnToLogin() {
        val intent = Intent(this@HomeActivity, StartActivity::class.java)
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
            getString(R.string.error_logout),
            Toast.LENGTH_LONG
        ).show()
        sharedPreferences.edit().remove("token").apply()
    }

}
