package com.cypress.bbcnewsapplication.data.repository

import com.cypress.bbcnewsapplication.data.dto.NewsDto
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.presentation.newsHeadline.SearchParams

sealed class NewsResource<T>(val data: T? = null , val message: String? = null){
    class Loading<T>() : NewsResource<T>()
    class Success<T>(data : T? = null) : NewsResource<T>(data)
    class Error<T>(data: T? = null , message: String? = null) : NewsResource<T>(data , message)
}

interface NewsHandlerRepository {
    suspend fun searchNews(searchParams: SearchParams) : NewsResource<NewsDto>
}

class NewsHandlerRepositoryImp(
    private val newsClientApi: NewsClientApi
): NewsHandlerRepository {

    override suspend fun searchNews(searchParams: SearchParams): NewsResource<NewsDto> {

        val response = newsClientApi.searchNewsTopHeadline(
            source = searchParams.source,
            query = searchParams.query,
            apiKey = searchParams.apiKey,
            page = searchParams.page
        )

        return NewsResource.Success(response)
    }

}