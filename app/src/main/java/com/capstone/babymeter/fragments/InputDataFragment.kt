package com.capstone.babymeter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.capstone.babymeter.R

class InputDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input_data, container, false)

        // Find the next button and set its click listener
        val nextButton: Button = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            // Get the FragmentManager
            val fragmentManager = parentFragmentManager

            // Create a transaction to replace the current fragment
            val transaction = fragmentManager.beginTransaction()

            // Specify the fragment to replace with and add it to backstack (optional)
            val nextFragment = UploadPictureFragment() // Replace with your actual fragment class
            transaction.replace(R.id.fragmentContainer, nextFragment) // Replace with your container ID
            transaction.addToBackStack(null) // Optional: add fragment to backstack for navigation history

            // Commit the transaction
            transaction.commit()
        }

        return view
    }
}