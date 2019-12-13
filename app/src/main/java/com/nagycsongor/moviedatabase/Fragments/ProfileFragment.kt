package com.nagycsongor.moviedatabase.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nagycsongor.moviedatabase.R

class ProfileFragment : Fragment {
    var bottomNavigationView: BottomNavigationView? = null
        private set
    private var sharedPreferences: SharedPreferences? = null
    constructor(bottomNavigationView: BottomNavigationView, sharedPreferences: SharedPreferences?){
        this.sharedPreferences = sharedPreferences
        this.bottomNavigationView = bottomNavigationView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutBotton = view.findViewById<Button>(R.id.logoutButton)
        logoutBotton.setOnClickListener{
            sharedPreferences?.edit()?.clear()?.commit()
            val fragmentTransaction =
                fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(
                R.id.fragment_frameLayout,
                LoginFragment(bottomNavigationView!!,sharedPreferences)
            )
            fragmentTransaction.commit()
            bottomNavigationView?.setTransitionVisibility(View.INVISIBLE)
        }
        return view
    }

}
