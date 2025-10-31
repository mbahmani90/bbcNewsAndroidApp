package com.cypress.bbcnewsapplication.di

import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.domain.repository.NewsHandlerRepository
import com.cypress.bbcnewsapplication.domain.repository.NewsHandlerRepositoryImp
import com.cypress.bbcnewsapplication.presentation.NewsHandlerViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val platformModules = module {

    single {
        Json { ignoreUnknownKeys = true }
    }

    single {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(NewsClientApi::class.java)
    }

    singleOf(::NewsHandlerRepositoryImp).bind<NewsHandlerRepository>()
    viewModelOf(::NewsHandlerViewModel)
}