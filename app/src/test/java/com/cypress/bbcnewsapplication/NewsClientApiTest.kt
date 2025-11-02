package com.cypress.bbcnewsapplication

import com.cypress.bbcnewsapplication.data.dto.NewsSource
import com.cypress.bbcnewsapplication.data.dto.SourceDto
import com.cypress.bbcnewsapplication.data.remote.NewsClientApi
import com.cypress.bbcnewsapplication.di.testPlatformModules
import io.mockk.coVerify
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class NewsClientApiTest : KoinTest {

    private val newsClientApi: NewsClientApi by inject()

    @Before
    fun setup() {
        startKoin {
            modules(testPlatformModules)
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun searchSources_shouldReturnMockedSourceDto() = runBlocking {

        val result: SourceDto = newsClientApi.searchSources("anyApiKey", "anyCategory")

        TestCase.assertEquals("200", result.status)
        TestCase.assertEquals(1, result.sources.size)
        val source: NewsSource = result.sources.first()
        TestCase.assertEquals("", source.id)
        TestCase.assertEquals("", source.name)

        coVerify(exactly = 1) { newsClientApi.searchSources(any(), any()) }
    }
}