package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.data.remote.NewsClientApiRxJava
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepository
import com.cypress.bbcnewsapplication.data.repository.NewsSourceRepositoryImp
import com.cypress.bbcnewsapplication.data.repository.SourceParams
import com.cypress.bbcnewsapplication.domain.model.SourceDomain
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class NewsSourceUseCaseTest : KoinTest {

    private lateinit var mockServer: MockWebServer
    private val newsSourceUseCase: NewsSourceUseCase by inject()

    @Before
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()
        val jsonResponse = File("src/test/resources/mock_news_source_response.json").readText()
        mockServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        startKoin {
            modules(
                module {

                    single { Json { ignoreUnknownKeys = true } }

                    single<NewsClientApi> {
                        val json = Json { ignoreUnknownKeys = true }
                        Retrofit.Builder()
                            .baseUrl(mockServer.url("/"))
                            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                            .build()
                            .create(NewsClientApi::class.java)
                    }
                    single<NewsClientApiRxJava>  {
                        val json: Json = get()
                        Retrofit.Builder()
                            .baseUrl("https://newsapi.org/")
                            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .build()
                            .create(NewsClientApiRxJava::class.java)
                    }
                    single { NewsSourceRepositoryImp(get(), get()) as NewsSourceRepository }
                    single { NewsSourceUseCase(get()) }
                }
            )
        }
    }

    @After
    fun teardown() {
        stopKoin()
        mockServer.shutdown()
    }

    @Test
    fun searchSources_parses_response_correctly() = runTest {

        val emissions = newsSourceUseCase(SourceParams(category = "xxx", apiKey = "xxx"))
            .take(2)
            .toList()

        val firstEmission = emissions[0]
        val secondEmission = emissions[1]

        TestCase.assertTrue(firstEmission is NewsResource.Loading)
        TestCase.assertTrue(secondEmission is NewsResource.Success<SourceDomain>)
        assertEquals("200", secondEmission.data?.status)
        assertEquals(7, secondEmission.data?.sources?.size)
        assertEquals("aftenposten", secondEmission.data?.sources?.drop(2)?.first()?.id)
        assertEquals("Argaam", secondEmission.data?.sources?.drop(5)?.first()?.name)

    }
}