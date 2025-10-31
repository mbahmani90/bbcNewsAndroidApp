package com.cypress.bbcnewsapplication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypress.bbcnewsapplication.data.dto.NewsDto
import com.cypress.bbcnewsapplication.domain.repository.NewsHandlerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class NewsHandlerState(
    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var newsDto: NewsDto? = null
)

data class SearchParams(
    var source: String,
    var apiKey: String,
    var page: Int = 1
)

class NewsHandlerViewModel(
    private val newsHandlerRepository: NewsHandlerRepository
) : ViewModel() {

    private var _newsHandlerState = mutableStateOf(NewsHandlerState())
    val newsHandlerState : State<NewsHandlerState> = _newsHandlerState


    fun searchNewsHeadline(searchParams: SearchParams) {

        viewModelScope.launch(Dispatchers.IO){
            _newsHandlerState.value = _newsHandlerState.value.copy(
                isLoading = true , isSuccess = false , errorMessage = "" , newsDto = null)
            try{
                val response = newsHandlerRepository.searchNews(searchParams)
                _newsHandlerState.value = _newsHandlerState.value.copy(
                    isLoading = false, isSuccess = true , errorMessage = "" ,
                    newsDto = response.data
                )
            }catch(e: Exception){
                _newsHandlerState.value = _newsHandlerState.value.copy(
                    isLoading = false, isSuccess = false , errorMessage = "${e.message}" ,
                    newsDto = null
                )
            }
        }

    }

}