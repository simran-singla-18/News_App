package com.example.news_app.Model

data class Article(
        val description: String,
        val publishedAt: String,
        val source: Source,
        val title: String,
        val urlToImage: String
)