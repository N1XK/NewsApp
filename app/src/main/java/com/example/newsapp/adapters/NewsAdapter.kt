package com.example.newsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleItemBinding
import com.example.newsapp.models.Article

class NewsAdapter(private val onItemClicked: (Article) -> Unit) : ListAdapter<Article, NewsAdapter.ArticleViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
) {
    class ArticleViewHolder(private var binding: ItemArticleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, context: Context) {
            Glide.with(context).load(article.urlToImage).into(binding.ivArticleImage)
            binding.tvTitle.text = article.title
            binding.tvDescription.text = article.description
            binding.tvSource.text = article.source?.name
            binding.tvPublishAt.text = article.publishedAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticleItemBinding.inflate(LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentArticle)
        }
        holder.bind(currentArticle, holder.itemView.context)
    }
}