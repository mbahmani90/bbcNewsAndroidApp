package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.dto.SourceDto
import com.cypress.bbcnewsapplication.data.dto.toDomain
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepository
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import com.cypress.bbcnewsapplication.domain.model.SourceDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsSourceUseCase(
    private val newsSourceRepository: NewsSourceRepository
) {

    fun invoke(sourceParams: SourceParams) : Flow<NewsResource<SourceDomain>> = flow {

        try {
            emit(NewsResource.Loading())
            val data = newsSourceRepository.getSources(sourceParams).data
            data?.let { sourceDto ->
                val sourceDomain = sourceDto.toDomain()
                emit(NewsResource.Success(sourceDomain))
            }
        }catch (e: Exception){
            emit(NewsResource.Error(null, e.message))
        }

    }

}