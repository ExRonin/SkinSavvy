package com.dicoding.skinSavvy.view.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.skinSavvy.databinding.PredictionHistoryBinding
import com.dicoding.skinSavvy.local.database.PredictionHistoryDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PredictionHistoryActivity : AppCompatActivity() {
    private lateinit var binding: PredictionHistoryBinding
    private lateinit var adapter: PredictionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PredictionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        displayPredictionHistory()
    }

    private fun setupRecyclerView() {
        adapter = PredictionHistoryAdapter(this)
        binding.recyclerViewHistory.adapter = adapter
        binding.recyclerViewHistory.layoutManager = LinearLayoutManager(this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun displayPredictionHistory() {
        val dao = PredictionHistoryDatabase.getDatabase(this).predictionHistoryDao()
        GlobalScope.launch(Dispatchers.IO) {
            val historyList = dao.getAll()
            withContext(Dispatchers.Main) {
                adapter.setPredictionHistory(historyList)
            }
        }
    }
}
