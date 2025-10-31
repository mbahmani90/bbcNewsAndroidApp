package com.cypress.bbcnewsapplication.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(configuration: KoinAppDeclaration? = null){
    startKoin{
        configuration?.invoke(this)
        modules(platformModules)
    }
}