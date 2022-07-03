package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.BreakingNewsPagingSources
import com.example.newsapp.models.SearchNewsPagingSources
import com.example.newsapp.util.Constants.Companion.MAX_NETWORK_PAGING_SIZE
import com.example.newsapp.util.Constants.Companion.NETWORK_PAGING_SIZE

class NewsRepository(
    private val service: NewsApiService,
    private val database: ArticleDatabase) {

    fun getBreakingNews(countryCode: String) =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BreakingNewsPagingSources(service, countryCode) }
        ).liveData

    fun getSearchNews(searchQuery: String) =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchNewsPagingSources(service, searchQuery) }
        ).liveData

    suspend fun upsert(article: Article) = database.getArticleDao().upsert(article)

    fun getSavedNews() = database.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = database.getArticleDao().deleteArticle(article)
}