package com.smarttoolfactory.data.api

import com.smarttoolfactory.data.model.remote.PropertyDTO
import com.smarttoolfactory.data.model.remote.PropertyResponse
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_ORDER_BY_BEDS_DESCENDING
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_ORDER_BY_PRICE_ASCENDING
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH_PAGE_1
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import java.io.IOException
import java.net.HttpURLConnection
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.QueueDispatcher
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Abstract class fo testing api with [MockWebServer] and [JUnit5]
 */
abstract class AbstractPropertyApiTest {

    /*
        Responses as String for setting response body for MockWebServer
     */
    lateinit var mockWebServer: MockWebServer

    val responseBodyOrderByNone by lazy {
        getResourceAsText(RESPONSE_JSON_PATH)
    }

    val responseBodyOrderedByPriceAscending by lazy {
        getResourceAsText(RESPONSE_JSON_PATH_ORDER_BY_PRICE_ASCENDING)
    }

    val responseBodyOrderedBedsDescending by lazy {
        getResourceAsText(RESPONSE_JSON_PATH_ORDER_BY_BEDS_DESCENDING)
    }

    // Page1 with no order filter
    val responseBodyPage1 by lazy {
        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
    }

    // Page with no order filter
    val responseBodyPage2 by lazy {
        getResourceAsText(RESPONSE_JSON_PATH_PAGE_1)
    }

    /*
        DTO list of REST response
     */
    val propertyListOrderByNone by lazy {
        convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!.res
    }

    val propertyListOrderByPriceAscending by lazy {
        convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(RESPONSE_JSON_PATH_ORDER_BY_PRICE_ASCENDING)
        )!!.res
    }

    val propertyListOrderByBedsDescending by lazy {
        convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(
                RESPONSE_JSON_PATH_ORDER_BY_BEDS_DESCENDING
            )
        )!!.res
    }

    val propertyListPage1 by lazy {
        convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(
                RESPONSE_JSON_PATH_PAGE_1
            )
        )!!.res
    }

    val propertyListPage2 by lazy {
        convertToObjectFromJson<PropertyResponse>(
            getResourceAsText(
                RESPONSE_JSON_PATH_PAGE_1
            )
        )!!.res
    }

    @BeforeEach
    open fun setUp() {

        mockWebServer = MockWebServer()
            .apply {
                start()
                dispatcher = PropertyDispatcher()
            }
    }

    @AfterEach
    open fun tearDown() {
        println("AbstractPostApiTest TEAR DOWN")
        mockWebServer.shutdown()
    }

    @Throws(IOException::class)
    fun enqueueResponse(
        code: Int = 200,
        headers: Map<String, String>? = null,
        responseBody: String = responseBodyOrderByNone
    ): MockResponse {

        // Define mock response
        val mockResponse = MockResponse()
        // Set response code
        mockResponse.setResponseCode(code)

        // Set headers
        headers?.let {
            for ((key, value) in it) {
                mockResponse.addHeader(key, value)
            }
        }

        // Set body
        mockWebServer.enqueue(
            mockResponse.setBody(responseBody)
        )
        println(
            "🍏 enqueueResponse() ${Thread.currentThread().name}," +
                " ${responseBodyOrderByNone.length} $mockResponse"
        )
        return mockResponse
    }

    /**
     * Dispatcher for [PropertyDTO] to return result based on ***search*** and ***ob***  parameters
     * used with query
     */
    inner class PropertyDispatcher : QueueDispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {

            return when {

                // Query with order by price descending
                request.path!!.contains("ob=pa") -> {
                    enqueueResponse(
                        HttpURLConnection.HTTP_OK,
                        responseBody = responseBodyOrderedByPriceAscending
                    )
                }
                // Query with order by beds descending
                request.path!!.contains("ob=bd") -> {
                    enqueueResponse(
                        HttpURLConnection.HTTP_OK,
                        responseBody = responseBodyOrderedBedsDescending
                    )
                }
                // Page 1 with no order by param
                request.path!!.contains("page=1") && request.path!!.endsWith("ob=") -> {
                    enqueueResponse(
                        HttpURLConnection.HTTP_OK,
                        responseBody = responseBodyPage1
                    )
                }
                // Page 2 with no order by param
                request.path!!.contains("page=2") && request.path!!.endsWith("ob=") -> {
                    enqueueResponse(
                        HttpURLConnection.HTTP_OK,
                        responseBody = responseBodyPage2
                    )
                }
                else -> {
                    enqueueResponse(HttpURLConnection.HTTP_OK)
                }
            }
        }
    }
}
