package com.cypress.bbcnewsapplication.data.repository

import com.cypress.bbcnewsapplication.data.dto.SourceDto
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.data.remote.NewsClientApiRxJava
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable

data class SourceParams(
    var category: String,
    var apiKey: String
)

interface NewsSourceRepository {

    suspend fun getSources(sourceParams: SourceParams): NewsResource<SourceDto>
    fun getSourcesRxJava(sourceParams: SourceParams): Flowable<SourceDto>

}

class NewsSourceRepositoryImp(
    private val newsClientApi: NewsClientApi,
    private val newsClientApiRxJava: NewsClientApiRxJava
) : NewsSourceRepository {

    override suspend fun getSources(sourceParams: SourceParams): NewsResource<SourceDto> {

        val sourceDto = newsClientApi.searchSources(sourceParams.apiKey ,
            sourceParams.category)

        return NewsResource.Success(sourceDto)
    }

    override fun getSourcesRxJava(sourceParams: SourceParams): Flowable<SourceDto> {

        val sourceObservable = newsClientApiRxJava.searchSources(sourceParams.apiKey ,
            sourceParams.category).toFlowable(BackpressureStrategy.BUFFER)

        return sourceObservable
    }

}