package com.capstone.babymeter.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.babymeter.R
import com.capstone.babymeter.ui.home.HomeFragment
import com.capstone.babymeter.ui.predictions.InputDataFragment
import com.capstone.babymeter.ui.profil.ProfilFragment
import com.capstone.babymeter.ui.history.HistoryFragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"Home", R.drawable.baseline_home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Camera", R.drawable.baseline_camera)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"History", R.drawable.baseline_history)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(4,"Profile", R.drawable.baseline_person)
        )

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> replaceFragment(HomeFragment())
                2 -> replaceFragment(InputDataFragment())
                3 -> replaceFragment(HistoryFragment())
                4 -> replaceFragment(ProfilFragment())
            }
        }

        // Check if user is logged in
        if (auth.currentUser != null) {
            // Load ProfileFragment if the user is logged in
            replaceFragment(ProfilFragment())
            bottomNavigation.show(4)
        } else {
            // Default Bottom Tab Selected
            replaceFragment(HomeFragment())
            bottomNavigation.show(1)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
