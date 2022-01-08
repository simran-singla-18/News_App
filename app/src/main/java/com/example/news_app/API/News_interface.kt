package com.example.news_app.API

import com.example.news_app.util.Constants.Companion.API_KEY
import com.example.news_app.Model.NewsData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface News_interface {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String="us",
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsData>

    @GET("v2/everything")
    suspend fun getNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int=1,
        @Query("apiKey")
        apiKey:String=API_KEY
    ):Response<NewsData>

}