package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.dto.toDomain
import com.cypress.bbcnewsapplication.data.repository.NewsHeadlineRepository
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.presentation.newsHeadline.SearchParams
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NewsHeadLineUseCase(
    private val newsHeadlineRepository: NewsHeadlineRepository
) {
    operator fun invoke(searchParams: SearchParams): Flow<NewsResource<NewsDomain>> =
        flow {

            emit(NewsResource.Loading())
            val newsDomain = newsHeadlineRepository.searchNews(searchParams).data?.toDomain()
            emit(NewsResource.Success(newsDomain))

        }.catch{ e ->
            emit(NewsResource.Error(null , e.message))
        }
        .flowOn(Dispatchers.IO)
}

class NewsHeadLineUseCaseRxJava(
    private val newsHeadlineRepository: NewsHeadlineRepository
){
    operator fun invoke(searchParams: SearchParams) : Flowable<NewsResource<NewsDomain>> {
        return newsHeadlineRepository.searchNewsRxJava(searchParams)
            .map<NewsResource<NewsDomain>> { newsDto ->
                NewsResource.Success(newsDto.toDomain())
            }
            .startWithItem(NewsResource.Loading())
            .onErrorReturn { throwable ->
                NewsResource.Error(null, throwable.message ?: "Unknown error")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}