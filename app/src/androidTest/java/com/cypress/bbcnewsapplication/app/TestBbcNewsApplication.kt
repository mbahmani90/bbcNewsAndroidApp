package com.cypress.bbcnewsapplication.app

import android.app.Application
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.di.testPlatformModules
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import retrofit2.Retrofit

class TestBbcNewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TestBbcNewsApplication)
            modules(testPlatformModules)
        }
    }



    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}