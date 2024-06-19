package com.capstone.babymeter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.capstone.babymeter.R
import com.capstone.babymeter.fragments.InputDataFragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the nextButton Button and set its click listener
        val nextButton: Button = view.findViewById(R.id.buttoncam)
        nextButton.setOnClickListener {
            // Replace current fragment with InputDataFragment
            replaceFragment(InputDataFragment())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        // Get the FragmentManager
        val fragmentManager = parentFragmentManager

        // Begin a FragmentTransaction
        val transaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the new fragment
        transaction.replace(R.id.fragmentContainer, fragment)

        // Optionally, add the transaction to the back stack
        transaction.addToBackStack(null)

        // Commit the transaction
        transaction.commit()
    }
}
