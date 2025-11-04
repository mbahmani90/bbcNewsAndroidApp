package com.cypress.bbcnewsapplication.presentation.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import com.cypress.bbcnewsapplication.domain.model.SourceDomain
import com.cypress.bbcnewsapplication.domain.usecase.NewsSourceUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import com.cypress.bbcnewsapplication.domain.usecase.NewsSourceUseCaseRxJava
import io.reactivex.rxjava3.disposables.CompositeDisposable

data class SourceState(
    var isLoading : Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String = "",
    var sourceDomain: SourceDomain? = null
)

data class CategoryDataClass(
    var name: String,
    var isSelected: Boolean = false
)

class SourceViewModel(
    private val newsSourceUseCase: NewsSourceUseCase,
    private val newsSourceUseCaseRxJava: NewsSourceUseCaseRxJava
) : ViewModel() {

    private val _categoryListState = mutableStateListOf(
        CategoryDataClass("Business"),
        CategoryDataClass("Entertainment"),
        CategoryDataClass("General"),
        CategoryDataClass("Health"),
        CategoryDataClass("Science"),
        CategoryDataClass("Sports"),
        CategoryDataClass("Technology"))

    val categoryListState: State<List<CategoryDataClass>> = derivedStateOf {
        _categoryListState.toList()
    }

    private var _sourceState = mutableStateOf(SourceState())

    val sourceState : State<SourceState> = _sourceState
    private val compositeDisposable = CompositeDisposable()

    fun getSources(sourceParams: SourceParams){
        getSourcesCoroutine(sourceParams)
//        getSourcesRxJava(sourceParams)
    }

    fun getSourcesCoroutine(sourceParams: SourceParams){
        newsSourceUseCase(sourceParams).onEach { result ->
            when(result){
                is NewsResource.Error -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = false, isSuccess = false,
                        errorMessage = "", sourceDomain = null)
                }
                is NewsResource.Loading -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = true, isSuccess = false,
                        errorMessage = "")
                }
                is NewsResource.Success -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = false, isSuccess = true,
                        errorMessage = "", sourceDomain = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getSourcesRxJava(sourceParams: SourceParams){
        val disposable = newsSourceUseCaseRxJava(sourceParams)
            .subscribe{ result ->
            when(result){
                is NewsResource.Error -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = false, isSuccess = false,
                        errorMessage = "", sourceDomain = null)
                }
                is NewsResource.Loading -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = true, isSuccess = false,
                        errorMessage = "")
                }
                is NewsResource.Success -> {
                    _sourceState.value = _sourceState.value.copy(
                        isLoading = false, isSuccess = true,
                        errorMessage = "", sourceDomain = result.data)
                }
            }
        }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun toggleCategoryItemSelection(name: String) : Boolean {
        val index = _categoryListState.indexOfFirst { it.name == name }
        if (index != -1) {
            val isCurrentlySelected = _categoryListState[index].isSelected

            for (i in _categoryListState.indices) {
                if (_categoryListState[i].isSelected) {
                    _categoryListState[i] = _categoryListState[i].copy(isSelected = false)
                }
            }

            if (!isCurrentlySelected) {
                _categoryListState[index] = _categoryListState[index].copy(isSelected = true)
            }
            return _categoryListState[index].isSelected
        }
        return false
    }

    fun getSelectedCategory() : String? {
        val index = _categoryListState.indexOfFirst { it.isSelected }
        if (index != -1) {
            return _categoryListState[index].name
        }
        return null
    }

}