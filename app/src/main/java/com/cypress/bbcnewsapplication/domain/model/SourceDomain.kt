package com.cypress.bbcnewsapplication.domain.model

data class SourceDomain(
    val sources: List<NewsSourceDomain>,
    val status: String
)

data class NewsSourceDomain(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)