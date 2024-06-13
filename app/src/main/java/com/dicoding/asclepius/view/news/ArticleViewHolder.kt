package com.dicoding.skinSavvy.view.news
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.skinSavvy.databinding.ItemArticleBinding
import com.dicoding.skinSavvy.server.Article

class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.apply {
            titleTextView.text = article.title
            descriptionTextView.text = article.description
            Glide.with(itemView.context).load(article.imageUrl).into(imageView)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemArticleBinding.inflate(inflater, parent, false)
            return ArticleViewHolder(binding)
        }
    }
}