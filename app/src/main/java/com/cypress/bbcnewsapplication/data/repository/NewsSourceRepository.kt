package com.cypress.bbcnewsapplication.data.repository

import com.cypress.bbcnewsapplication.data.dto.SourceDto
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi

data class SourceParams(
    var category: String,
    var apiKey: String
)

interface NewsSourceRepository {

    suspend fun getSources(sourceParams: SourceParams): NewsResource<SourceDto>

}

class NewsSourceRepositoryImp(
    private val newsClientApi: NewsClientApi
) : NewsSourceRepository {

    override suspend fun getSources(sourceParams: SourceParams): NewsResource<SourceDto> {

        val sourceDto = newsClientApi.searchSources(sourceParams.apiKey ,
            sourceParams.category)

        return NewsResource.Success(sourceDto)
    }

}