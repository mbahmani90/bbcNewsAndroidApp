package com.cypress.bbcnewsapplication.data.remote

import com.cypress.bbcnewsapplication.data.dto.NewsDto
import com.cypress.bbcnewsapplication.data.dto.SourceDto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsClientApiRxJava {

    @GET("v2/top-headlines")
    fun searchNewsTopHeadline(
        @Query("sources") source: String,
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Observable<NewsDto>

    @GET("v2/top-headlines/sources")
    fun searchSources(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String
    ): Observable<SourceDto>

}