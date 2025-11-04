package com.cypress.bbcnewsapplication.app

import android.app.Application
import com.cypress.bbcnewsapplication.di.testPlatformModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

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