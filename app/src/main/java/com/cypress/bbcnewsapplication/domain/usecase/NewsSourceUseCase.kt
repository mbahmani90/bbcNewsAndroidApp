package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.dto.toDomain
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepository
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import com.cypress.bbcnewsapplication.domain.model.SourceDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NewsSourceUseCase(
    private val newsSourceRepository: NewsSourceRepository
) {

    operator fun invoke(sourceParams: SourceParams) : Flow<NewsResource<SourceDomain>> =
        flow {
            emit(NewsResource.Loading())
            val data = newsSourceRepository.getSources(sourceParams).data
            data?.let { sourceDto ->
                val sourceDomain = sourceDto.toDomain()
                emit(NewsResource.Success(sourceDomain))
            }
        }.catch { e ->
            emit(NewsResource.Error(null, e.message))
        }
        .flowOn(Dispatchers.IO)

}