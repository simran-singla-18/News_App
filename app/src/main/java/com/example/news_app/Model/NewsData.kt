package com.example.news_app.Model

import com.example.news_app.Model.Article

data class NewsData(
    val articles: MutableList<Article>,
    val totalResults:Int


)