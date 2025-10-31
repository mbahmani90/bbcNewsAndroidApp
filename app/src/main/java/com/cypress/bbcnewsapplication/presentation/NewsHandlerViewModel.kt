package com.cypress.bbcnewsapplication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypress.bbcnewsapplication.data.dto.NewsDto
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class NewsHandlerState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var newsDomain: NewsDomain? = null
)

data class SearchParams(
    var source: String,
    var apiKey: String,
    var page: Int = 1
)

class NewsHandlerViewModel(
    private val newsHandlerUserCass: NewsHeadLineUseCase
) : ViewModel() {

    private var _newsHandlerState = mutableStateOf(NewsHandlerState())
    val newsHandlerState : State<NewsHandlerState> = _newsHandlerState

    fun searchNewsHeadline(searchParams: SearchParams) {

        newsHandlerUserCass.invoke(searchParams).onEach { result ->
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

}