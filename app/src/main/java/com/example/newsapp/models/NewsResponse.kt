package com.example.newsapp.models

//from tutorial, but no use in current app
data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)