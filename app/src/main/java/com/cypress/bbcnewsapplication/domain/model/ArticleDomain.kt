package com.cypress.bbcnewsapplication.domain.model


data class ArticleDomain (
    val title : String?,
    val content: String?,
    val description : String?,
    val urlToImage : String?,
    val url: String?,
    val publishedAt : String?,
    val sourceName : String?
)