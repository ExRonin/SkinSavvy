package com.dicoding.skinSavvy.view.user

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.skinSavvy.local.user.PredictionHistory

class PredictionHistoryDiffCallback(
    private val oldList: List<PredictionHistory>,
    private val newList: List<PredictionHistory>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
