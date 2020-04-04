package com.example.trackyourstress_ba.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment

import com.example.trackyourstress_ba.R
import com.example.trackyourstress_ba.kotlin.ClearingUtils
import kotlinx.android.synthetic.main.activity_home.*

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (container != null) {
            ClearingUtils.clearSharedPreferences(container.context)
        }
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }
}