package com.cypress.bbcnewsapplication.data.remote

import com.cypress.bbcnewsapplication.data.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface NewsClientApi {

    @GET("v2/top-headlines")
    suspend fun searchNewsTopHeadline(
        @Query("sources") source: String,
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): NewsDto

}