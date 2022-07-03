package com.example.newsapp.models

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.util.Constants.Companion.START_NEWS_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class SearchNewsPagingSources(
    private val service: NewsApiService,
    private val searchQuery: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: START_NEWS_PAGE_INDEX
        return try {
            val response = service.getSearchNews(searchQuery, position)
            val articles = response.body()!!.articles
            LoadResult.Page(
                data = articles,
                prevKey = if (position == START_NEWS_PAGE_INDEX) null else position - 1,
                nextKey = if (articles.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)

        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}