package com.dicoding.skinSavvy.view.news
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinSavvy.server.Article


class NewsAdapter(private val articles: MutableList<Article>) :
    RecyclerView.Adapter<ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateData(newArticles: List<Article>) {
        val diffResult = DiffUtil.calculateDiff(ArticleDiffCallback(articles, newArticles))
        articles.clear()
        articles.addAll(newArticles)
        diffResult.dispatchUpdatesTo(this)
    }
}
