package com.nagycsongor.moviedatabase.Main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nagycsongor.moviedatabase.Fragments.*
import com.nagycsongor.moviedatabase.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                replaceFragment(MainFragment(sharedPreferences))
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                replaceFragment(ProfileFragment(bottomNavigationView, sharedPreferences))
                return@OnNavigationItemSelectedListener true
            }
            R.id.favorite -> {
                replaceFragment(FavoriteFragment(sharedPreferences))
                return@OnNavigationItemSelectedListener true
            }
            R.id.now -> {
                replaceFragment(NowPlayingFragment(sharedPreferences))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("com.nagycsongor.moviedatabase", Context.MODE_PRIVATE)

        replaceFragment(LoginFragment(bottomNavigationView, sharedPreferences))

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.setTransitionVisibility(View.INVISIBLE)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_frameLayout, fragment)
        fragmentTransaction.commit()
    }

}
