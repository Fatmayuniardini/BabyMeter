package com.capstone.babymeter.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.babymeter.R

class HistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = generateDummyHistoryList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)

        rvHistory = rootView.findViewById(R.id.rv_History)
        rvHistory.layoutManager = LinearLayoutManager(requireContext())

        historyAdapter = HistoryAdapter(historyList, requireContext())
        rvHistory.adapter = historyAdapter

        return rootView
    }

    private fun generateDummyHistoryList(): List<HistoryItem> {
        // Replace with actual implementation to fetch or generate history items
        return listOf(
            HistoryItem(
                "Name 1", "1234567890", "Description 1", "URL 1",
                "Lingkar Kepala 1", "Lingkar Badan 1", "Lingkar Kaki 1",
                "Tinggi Badan 1", "BMI 1", "Category 1"
            ),
            HistoryItem(
                "Name 2", "0987654321", "Description 2", "URL 2",
                "Lingkar Kepala 2", "Lingkar Badan 2", "Lingkar Kaki 2",
                "Tinggi Badan 2", "BMI 2", "Category 2"
            )
            // Add more items as needed
        )
    }
}