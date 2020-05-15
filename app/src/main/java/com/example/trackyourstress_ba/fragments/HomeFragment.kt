package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R

/**
 * The class managing the HomeFragment in the navigation drawer. Same as the HomeActivity GUI but called
 * from the Navigation Drawer not after Login.
 */
class HomeFragment : Fragment() {
    lateinit var currentContext: Context
    /**
     * general creation method for the HomeFragment. Is called before it is displayed.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        this.activity?.title = getString(R.string.home)
        currentContext = view!!.context

        return view
    }


}