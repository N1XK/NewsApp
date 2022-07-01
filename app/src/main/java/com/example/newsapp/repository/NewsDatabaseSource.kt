package com.example.newsapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.database.ArticleDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.util.Constants.Companion.NEXT_PAGING_SIZE
import kotlinx.coroutines.flow.Flow

class NewsDatabaseSource(val database: ArticleDatabase) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.NewsAPI.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.NewsAPI.getSearchNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = database.getArticleDao().upsert(article)

    fun getSavedNews() = database.getArticleDao().getAllArticles()

    suspend fun delete(article: Article) = database.getArticleDao().deleteArticle(article)

//    fun getBreakingNew(countryCode: String): Flow<PagingData<Article>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NEXT_PAGING_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { BreakingNewsPagingSource(countryCode)}
//        ).flow
//    }
}