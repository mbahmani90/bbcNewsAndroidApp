package com.cypress.bbcnewsapplication.data.dto

import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.domain.model.NewsSourceDomain
import com.cypress.bbcnewsapplication.domain.model.SourceDomain
import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val sources: List<NewsSource>,
    val status: String
)

@Serializable
data class NewsSource(
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String
)

fun NewsSource.toDomain() : NewsSourceDomain{
    return NewsSourceDomain(
        category = category,
        country = country,
        description = description,
        id = id,
        language = language,
        name = name,
        url = url
    )
}

fun SourceDto.toDomain(): SourceDomain{
    return SourceDomain(
        status = status,
        sources = sources.map {
            it.toDomain()
        }
    )

}