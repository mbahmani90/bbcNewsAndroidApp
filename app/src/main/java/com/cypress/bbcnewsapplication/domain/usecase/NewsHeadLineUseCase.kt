package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.dto.toDomain
import com.cypress.bbcnewsapplication.data.repository.NewsHandlerRepository
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.presentation.newsHeadline.SearchParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class NewsHeadLineUseCase(
    private val newsHandlerRepository: NewsHandlerRepository
) {
    operator fun invoke(searchParams: SearchParams): Flow<NewsResource<NewsDomain>> =
        flow {

            emit(NewsResource.Loading())
            val newsDomain = newsHandlerRepository.searchNews(searchParams).data?.toDomain()
            emit(NewsResource.Success(newsDomain))

        }.catch{ e ->
            emit(NewsResource.Error(null , e.message))
        }
}