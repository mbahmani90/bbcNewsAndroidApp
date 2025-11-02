package com.cypress.bbcnewsapplication

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.cypress.bbcnewsapplication.app.TestBbcNewsApplication

class TestBbcNewsRunner: AndroidJUnitRunner() {

    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestBbcNewsApplication::class.java.name, context)
    }
}