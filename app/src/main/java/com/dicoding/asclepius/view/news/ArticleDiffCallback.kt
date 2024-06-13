package com.dicoding.skinSavvy.view.news

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.skinSavvy.server.Article

 class ArticleDiffCallback(
    private val oldList: List<Article>,
    private val newList: List<Article>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}