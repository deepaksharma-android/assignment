package com.example.assignment.data.repository

import com.example.assignment.data.remote.api.HoldingsApi
import com.example.assignment.test.BaseTest
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HoldingsRepositoryImplTest : BaseTest() {

    private lateinit var server: MockWebServer
    private lateinit var api: HoldingsApi
    private lateinit var repo: HoldingsRepositoryImpl

    @Before
    fun setupRepo() {
        server = MockWebServer().apply { start() }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
        api = retrofit.create(HoldingsApi::class.java)
        // Provide a trivial dispatcher for repository under test
        repo = HoldingsRepositoryImpl(api, ioDispatcher = dispatcher)
    }

    @After
    fun tearDownRepo() {
        server.shutdown()
    }

    @Test
    fun getHoldings_success_mapsToDomain() = kotlinx.coroutines.test.runTest(dispatcher) {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(loadJson("holdings_success.json"))
        )

        val list = repo.getHoldings(page = 1, pageSize = 50)
        assertTrue(list.isNotEmpty())
        assertEquals("MAHABANK", list.first().symbol)
        assertEquals(38.05, list.first().lastTradedPrice ?: 0.0, 0.0)
    }

    @Test(expected = HttpException::class)
    fun getHoldings_serverError_throws() = kotlinx.coroutines.test.runTest(dispatcher) {
        server.enqueue(MockResponse().setResponseCode(500))
        repo.getHoldings(page = 1, pageSize = 50)
    }
}


