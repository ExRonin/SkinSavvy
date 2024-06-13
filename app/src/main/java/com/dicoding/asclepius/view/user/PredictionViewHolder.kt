package com.dicoding.skinSavvy.view.user
import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinSavvy.R
import com.dicoding.skinSavvy.databinding.ItemPredictionHistoryBinding
import com.dicoding.skinSavvy.local.user.PredictionHistory

class PredictionViewHolder(
    private val binding: ItemPredictionHistoryBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(prediction: PredictionHistory) {
        Glide.with(binding.root)
            .load(Uri.parse(prediction.imagePath))
            .into(binding.imageViewHistory)
        binding.textViewHistory.text = context.getString(
            R.string.result_text,
            prediction.label,
            prediction.confidence * 100
        )
    }
}
