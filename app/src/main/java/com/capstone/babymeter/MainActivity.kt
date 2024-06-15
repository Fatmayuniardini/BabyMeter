package com.capstone.babymeter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.babymeter.fragments.HomeFragment
import com.capstone.babymeter.fragments.HistoryFragment
import com.capstone.babymeter.fragments.InputDataFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"Home",R.drawable.baseline_home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Camera",R.drawable.baseline_camera)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"History",R.drawable.baseline_history)
        )

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                    replaceFragment(HomeFragment())
                }
                2 -> {
                    replaceFragment(InputDataFragment())
                }
                3 -> {
                    replaceFragment(HistoryFragment())
                }
            }
        }

        // default Bottom Tab Selected
        replaceFragment(HomeFragment())
        bottomNavigation.show(1)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }
}