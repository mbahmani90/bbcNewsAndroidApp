package com.cypress.bbcnewsapplication.domain.usecase

import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.data.repository.NewsHandlerRepository
import com.cypress.bbcnewsapplication.data.repository.NewsHandlerRepositoryImp
import com.cypress.bbcnewsapplication.data.repository.NewsResource
import com.cypress.bbcnewsapplication.domain.model.NewsDomain
import com.cypress.bbcnewsapplication.presentation.newsHeadline.SearchParams
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
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import java.io.File
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class NewsHeadlineUseCaseTest : KoinTest {

    private lateinit var mockWebServer: MockWebServer
    private val newsHeadlineUseCase : NewsHeadLineUseCase by inject()


    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val jsonResponse = File("src/test/resources/mock_news_headline_response.json").readText()
        mockWebServer.enqueue(MockResponse().setBody(jsonResponse).setResponseCode(200))

        startKoin {
            modules(
                module {
                    single<NewsClientApi> {
                        val json = Json { ignoreUnknownKeys = true }
                        Retrofit.Builder()
                            .baseUrl(mockWebServer.url("/"))
                            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                            .build()
                            .create(NewsClientApi::class.java)
                    }
                    single { NewsHandlerRepositoryImp(get()) as NewsHandlerRepository }
                    single { NewsHeadLineUseCase(get()) }
                }
            )
        }
    }

    @After
    fun tearDown(){
        stopKoin()
        mockWebServer.shutdown()
    }

    @Test
    fun newsHeadline_parses_response_correctly() = runTest{
        val emissions = newsHeadlineUseCase(SearchParams("bbc-news" , "" , "xxxx"))
            .take(2)
            .toList()

        val firstEmission = emissions[0]
        val secondEmission = emissions[1]

        TestCase.assertTrue(firstEmission is NewsResource.Loading)
        TestCase.assertTrue(secondEmission is NewsResource.Success<NewsDomain>)
        assertEquals("ok", secondEmission.data?.status)
        assertEquals(10, secondEmission.data?.articles?.size)
        assertEquals("King wants Andrew to lose last military title, minister says",
            secondEmission.data?.articles?.drop(2)?.first()?.title)
        assertEquals("Nine people have life-threatening injuries, police say, with counter-terror police joining the investigation.",
            secondEmission.data?.articles?.drop(5)?.first()?.description)


    }

}