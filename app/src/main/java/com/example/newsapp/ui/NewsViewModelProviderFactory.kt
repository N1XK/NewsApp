package com.example.newsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.NewsDatabaseSource

class NewsViewModelProviderFactory(
    private val application: Application,
    private val newsRepository: NewsDatabaseSource
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(application, newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}