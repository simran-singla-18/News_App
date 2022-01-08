package com.example.news_app.Repository

import com.example.news_app.API.RetrofitInstance

class NewsRepository{
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
       RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber:Int)=
       RetrofitInstance.api.getNews(searchQuery,pageNumber)
}