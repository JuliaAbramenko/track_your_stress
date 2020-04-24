package com.example.trackyourstress_ba.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trackyourstress_ba.R

class HomeFragment : Fragment() {
    lateinit var currentContext: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        this.activity?.title = getString(R.string.home)
        currentContext = requireView().context

        return view
    }


}