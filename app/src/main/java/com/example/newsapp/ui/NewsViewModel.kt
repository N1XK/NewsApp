package com.example.newsapp.ui

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.models.Article
import com.example.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel(
    application: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(application) {

    //Save RecyclerView State
    private lateinit var state: Parcelable
    fun saveRecyclerViewState(parcelable: Parcelable) { state = parcelable }
    fun restoreRecyclerViewState() : Parcelable = state
    fun stateInitialized() : Boolean = ::state.isInitialized

    private val currentQuery = MutableLiveData<String>()

    init {
        breakingNews()
    }

    fun breakingNews(): LiveData<PagingData<Article>> {
        return newsRepository.getBreakingNews("us").cachedIn(viewModelScope)
    }

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