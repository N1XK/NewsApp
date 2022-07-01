//package com.example.newsapp.repository
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.newsapp.api.RetrofitInstance
//import com.example.newsapp.models.Article
//import com.example.newsapp.util.Constants.Companion.NEXT_PAGING_SIZE
//import com.example.newsapp.util.Constants.Companion.START_NEWS_PAGE_NUMBER
//import retrofit2.HttpException
//
//class BreakingNewsPagingSource(val countryCode: String) : PagingSource<Int, Article>() {
//    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
//        val pageNumber = params.key ?: START_NEWS_PAGE_NUMBER
//        return try {
//            val response = RetrofitInstance.NewsAPI.getBreakingNews(countryCode, pageNumber)
//            val articles = response.body()!!.articles
//            val nextPageNumber: Int? = null
//            if (articles.isNotEmpty()) {
//                pageNumber + (params.loadSize / NEXT_PAGING_SIZE)
//            }
//            LoadResult.Page(
//                data = articles,
//                prevKey = if (pageNumber == START_NEWS_PAGE_NUMBER) null else pageNumber,
//                nextKey = nextPageNumber
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        } catch (e: HttpException) {
//            LoadResult.Error(e)
//        }
//    }
//}
//
//
////suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
////    RetrofitInstance.NewsAPI.getBreakingNews(countryCode, pageNumber)
////
////suspend fun getSearchNews(searchQuery: String, pageNumber: Int) =
////    RetrofitInstance.NewsAPI.getSearchNews(searchQuery, pageNumber)