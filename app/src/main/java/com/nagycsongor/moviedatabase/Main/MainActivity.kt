package com.nagycsongor.moviedatabase.Main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nagycsongor.moviedatabase.Fragments.*
import com.nagycsongor.moviedatabase.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.home ->{
                replaceFragment(MainFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile ->{
                replaceFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.favorite ->{
                replaceFragment(FavoriteFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.now ->{
                replaceFragment(NowPlayingFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(LoginFragment(bottomNavigationView))

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.setTransitionVisibility(View.INVISIBLE)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_frameLayout, fragment)
        fragmentTransaction.commit()
    }

}
