package com.example.newsapp.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    private val currentQuery = MutableLiveData<String>()

    val breakingNews = newsRepository.getBreakingNews("us").cachedIn(viewModelScope)

    val searchNews = currentQuery.switchMap { searchQuery ->
        newsRepository.getSearchNews(searchQuery).cachedIn(viewModelScope)
    }

    fun getSearchNews(searchQuery: String) {
        currentQuery.value = searchQuery
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }
}