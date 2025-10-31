package com.cypress.bbcnewsapplication.domain.model

data class NewsDomain(
    val articles: List<ArticleDomain>,
    val totalResults: Int,
    val status: String
)