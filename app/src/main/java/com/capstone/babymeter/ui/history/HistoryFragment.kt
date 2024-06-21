package com.capstone.babymeter.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.babymeter.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HistoryFragment : Fragment() {

    private lateinit var rvHistory: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private val historyList = mutableListOf<HistoryItem>()
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)

        auth = FirebaseAuth.getInstance()
        rvHistory = rootView.findViewById(R.id.rv_History)
        rvHistory.layoutManager = LinearLayoutManager(requireContext())

        historyAdapter = HistoryAdapter(historyList, requireContext())
        rvHistory.adapter = historyAdapter

        fetchHistoryData()

        return rootView
    }

    private fun fetchHistoryData() {
        val user = auth.currentUser
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                idToken?.let { token ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val documents = db.collection("predictions").get().await()
                            for (document in documents) {
                                val name = document.getString("babyName") ?: "N/A"
                                val nik = document.getString("nik") ?: "N/A"
                                val imageUrl = document.getString("imageUrl") ?: "N/A"
                                historyList.add(HistoryItem(name, nik, imageUrl))
                            }
                            CoroutineScope(Dispatchers.Main).launch {
                                historyAdapter.notifyDataSetChanged()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
    fun removeItem(nik: String) {
        // Find the item in the list
        val index = historyList.indexOfFirst { it.nik == nik }

        // If the item was found, remove it from the list and notify the adapter
        if (index != -1) {
            historyList.removeAt(index)
            historyAdapter.notifyItemRemoved(index)
        }
    }
}