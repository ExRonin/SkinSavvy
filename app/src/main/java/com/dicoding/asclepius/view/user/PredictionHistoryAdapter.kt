package com.dicoding.skinSavvy.view.user
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinSavvy.databinding.ItemPredictionHistoryBinding
import com.dicoding.skinSavvy.local.user.PredictionHistory

class PredictionHistoryAdapter(private val context: Context) :
    RecyclerView.Adapter<PredictionViewHolder>() {

    private var historyList: MutableList<PredictionHistory> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPredictionHistoryBinding.inflate(inflater, parent, false)
        return PredictionViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val prediction = historyList[position]
        holder.bind(prediction)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun setPredictionHistory(newHistoryList: List<PredictionHistory>) {
        val diffResult = DiffUtil.calculateDiff(PredictionHistoryDiffCallback(historyList, newHistoryList))
        historyList.clear()
        historyList.addAll(newHistoryList)
        diffResult.dispatchUpdatesTo(this)
    }


}
