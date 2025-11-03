package com.cypress.bbcnewsapplication.presentation.newsHeadline

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCase
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCaseRxJava
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class NewsHandlerState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var newsDomain: NewsDomain? = null
)

data class SearchParams(
    var source: String,
    var query: String,
    var apiKey: String,
    var page: Int = 1
)

class NewsHeadlineViewModel(
    private val newsHandlerUseCase: NewsHeadLineUseCase,
    private val newsHandlerUseCaseRxJava: NewsHeadLineUseCaseRxJava
) : ViewModel() {

    private var _newsHandlerState = mutableStateOf(NewsHandlerState())
    val newsHandlerState : State<NewsHandlerState> = _newsHandlerState

    val compositeDisposable = CompositeDisposable()

    fun searchNewsHeadline(searchParams: SearchParams){
//        searchNewsHeadlineCoroutine(searchParams)
        searchNewsHeadlineRxJava(searchParams)
    }

    fun searchNewsHeadlineCoroutine(searchParams: SearchParams) {

        newsHandlerUseCase(searchParams).onEach { result ->
            when(result){
                is NewsResource.Error -> {
                    _newsHandlerState.value = _newsHandlerState.value.copy(
                        isLoading = false, isSuccess = false , errorMessage = "${result.message}"
                    )
                }
                is NewsResource.Loading -> {
                    _newsHandlerState.value = _newsHandlerState.value.copy(
                        isLoading = true , isSuccess = false , errorMessage = "")
                }
                is NewsResource.Success -> {
                    _newsHandlerState.value = _newsHandlerState.value.copy(
                        isLoading = false, isSuccess = true , errorMessage = "" ,
                        newsDomain = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun searchNewsHeadlineRxJava(searchParams: SearchParams) {
        val disposable = newsHandlerUseCaseRxJava(searchParams)
            .subscribe { result ->
                when(result){
                    is NewsResource.Error -> {
                        _newsHandlerState.value = _newsHandlerState.value.copy(
                            isLoading = false, isSuccess = false , errorMessage = "${result.message}"
                        )
                    }
                    is NewsResource.Loading -> {
                        _newsHandlerState.value = _newsHandlerState.value.copy(
                            isLoading = true , isSuccess = false , errorMessage = "")
                    }
                    is NewsResource.Success -> {
                        _newsHandlerState.value = _newsHandlerState.value.copy(
                            isLoading = false, isSuccess = true , errorMessage = "" ,
                            newsDomain = result.data
                        )
                    }
                }
            }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}