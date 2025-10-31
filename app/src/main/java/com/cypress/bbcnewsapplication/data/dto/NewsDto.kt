package com.cypress.bbcnewsapplication.data.dto

import com.cypress.bbcnewsapplication.domain.model.ArticleDomain
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)

@Serializable
data class ArticleDto(
    val author: String? ,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)

@Serializable
data class Source(
    val id: String? = null,
    val name: String
)

fun ArticleDto.toDomain() : ArticleDomain {
    return ArticleDomain(
        title = title ?: "",
        content = content,
        description = description,
        urlToImage = urlToImage,
        url = url,
        publishedAt = publishedAt,
        sourceName = source?.name
    )
}

fun NewsDto.toDomain() : NewsDomain{
    return NewsDomain(
        status = status,
        totalResults = totalResults,
        articles = articles.map { articleDto ->
            articleDto.toDomain()
        }
    )
}