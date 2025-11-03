package com.cypress.bbcnewsapplication.di

import android.os.Build
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.data.remote.NewsClientApiRxJava
import com.cypress.bbcnewsapplication.data.repository.NewsHeadlineRepository
import com.cypress.bbcnewsapplication.data.repository.NewsHeadlineRepositoryImp
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepository
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepositoryImp
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCase
import com.cypress.bbcnewsapplication.domain.usecase.NewsHeadLineUseCaseRxJava
import com.cypress.bbcnewsapplication.domain.usecase.NewsSourceUseCase
import com.cypress.bbcnewsapplication.domain.usecase.NewsSourceUseCaseRxJava
import com.cypress.bbcnewsapplication.presentation.fingerPrint.FingerPrintInterface
import com.cypress.bbcnewsapplication.presentation.fingerPrint.FingerPrintInterfaceImp
import com.cypress.bbcnewsapplication.presentation.newsHeadline.NewsHeadlineViewModel
import com.cypress.bbcnewsapplication.presentation.sourceList.SourceViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

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

    single {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(NewsClientApiRxJava::class.java)
    }

    singleOf(::NewsHeadlineRepositoryImp).bind<NewsHeadlineRepository>()
    single { NewsHeadLineUseCase(get()) }
    single { NewsHeadLineUseCaseRxJava(get()) }
    viewModelOf(::NewsHeadlineViewModel)


    singleOf(::NewsSourceRepositoryImp).bind<NewsSourceRepository>()
    single { NewsSourceUseCase(get()) }
    single { NewsSourceUseCaseRxJava(get()) }
    viewModelOf(::SourceViewModel)

    factoryOf(::FingerPrintInterfaceImp).bind<FingerPrintInterface>()

    factory {
        val title = "title"
        val description = "description"

        val authenticators = if(Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticators)

        if(Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }
        promptInfo.build()
    }

}