package com.example.trackyourstress_ba.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.fragments.NotificationsFragment
import com.example.trackyourstress_ba.fragments.ProfileFragment
import com.example.trackyourstress_ba.fragments.QuestionnairesFragment
import com.example.trackyourstress_ba.fragments.StudyOverviewFragment
import com.example.trackyourstress_ba.kotlin.GlobalVariables
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawer : DrawerLayout
    lateinit var drawer_toggle : ActionBarDrawerToggle
    lateinit var nav_view : NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun onStart() {
        super.onStart()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        drawer_toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(drawer_toggle)
        drawer_toggle.syncState()
        nav_view = findViewById(R.id.nav_view)
        nav_view.setItemIconTintList(null)
        val profile_frag = ProfileFragment()
        val notifications_fragment = NotificationsFragment()
        val studies_fragment = StudyOverviewFragment()
        val questionnaires_fragment = QuestionnairesFragment()
        nav_view.setNavigationItemSelectedListener {item ->
            when(item.itemId) {

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
                R.id.nav_study_overview -> {
                    val transact = supportFragmentManager.beginTransaction()
                    //transact.addToBackStack(null)
                    transact.replace(R.id.fragment_container, studies_fragment)
                    transact.commit()
                    drawer.closeDrawers()
                    true
                }

                else -> true
            }

        }



    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        else super.onBackPressed()
    }


}
