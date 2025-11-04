package com.cypress.bbcnewsapplication.presentation.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCase
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCaseRxJava
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class NewsHeadlineState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var newsDomain: NewsDomain? = null,
    var query: TextFieldValue = TextFieldValue(""),
    var page: Int = 1
)

data class SearchParams(
    var source: String,
    var query: String,
    var apiKey: String,
    var page: Int = 1
)

class NewsHeadlineViewModel(
    private val newsHeadlineUseCase: NewsHeadLineUseCase,
    private val newsHeadlineUseCaseRxJava: NewsHeadLineUseCaseRxJava
) : ViewModel() {

    private var _newsHeadlineState = mutableStateOf(NewsHeadlineState())
    val newsHeadlineState : State<NewsHeadlineState> = _newsHeadlineState

    val compositeDisposable = CompositeDisposable()

    fun searchNewsHeadline(searchParams: SearchParams){
        searchNewsHeadlineCoroutine(searchParams)
//        searchNewsHeadlineRxJava(searchParams)
    }

    fun searchNewsHeadlineCoroutine(searchParams: SearchParams) {

        newsHeadlineUseCase(searchParams).onEach { result ->
            when(result){
                is NewsResource.Error -> {
                    _newsHeadlineState.value = _newsHeadlineState.value.copy(
                        isLoading = false, isSuccess = false , errorMessage = "${result.message}"
                    )
                }
                is NewsResource.Loading -> {
                    _newsHeadlineState.value = _newsHeadlineState.value.copy(
                        isLoading = true , isSuccess = false , errorMessage = "")
                }
                is NewsResource.Success -> {
                    _newsHeadlineState.value = _newsHeadlineState.value.copy(
                        isLoading = false, isSuccess = true , errorMessage = "" ,
                        newsDomain = result.data
                    )
                }
            }
        }.launchIn(viewModelScope)

    }

    fun searchNewsHeadlineRxJava(searchParams: SearchParams) {
        val disposable = newsHeadlineUseCaseRxJava(searchParams)
            .subscribe { result ->
                when(result){
                    is NewsResource.Error -> {
                        _newsHeadlineState.value = _newsHeadlineState.value.copy(
                            isLoading = false, isSuccess = false , errorMessage = "${result.message}"
                        )
                    }
                    is NewsResource.Loading -> {
                        _newsHeadlineState.value = _newsHeadlineState.value.copy(
                            isLoading = true , isSuccess = false , errorMessage = "")
                    }
                    is NewsResource.Success -> {
                        _newsHeadlineState.value = _newsHeadlineState.value.copy(
                            isLoading = false, isSuccess = true , errorMessage = "" ,
                            newsDomain = result.data
                        )
                    }
                }
            }
        compositeDisposable.add(disposable)
    }

    fun setPage(page: Int){
        _newsHeadlineState.value = _newsHeadlineState.value.copy(
            page = page
        )
    }

    fun setQuery(query: TextFieldValue){
        _newsHeadlineState.value = _newsHeadlineState.value.copy(
            query = query
        )
    }

    fun updateQueryCursor(){
        _newsHeadlineState.value = _newsHeadlineState.value.copy(
            query = _newsHeadlineState.value.query.copy(
                selection = TextRange(_newsHeadlineState.value.query.text.length))
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}