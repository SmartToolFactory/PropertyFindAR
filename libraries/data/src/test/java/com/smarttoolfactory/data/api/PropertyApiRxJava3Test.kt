package com.smarttoolfactory.data.api

import com.google.common.truth.Truth
import com.smarttoolfactory.data.model.remote.PropertyResponse
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.HttpURLConnection
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PropertyApiRxJava3Test : AbstractPropertyApiTest() {

    /**
     * Api is the SUT to test headers, url, response and DTO objects
     */
    private lateinit var api: PropertyApiRxJava

    @Test
    fun `Request should have correct url`() {

        // GIVEN
        enqueueResponse(HttpURLConnection.HTTP_OK)

        // WHEN
        api
            .getPropertyResponse()
            .blockingGet()

        val request = mockWebServer.takeRequest()

        // THEN
        Truth.assertThat(request.path).isEqualTo("/mobileapi/search?ob=")
    }

    @Test
    fun `Request should have correct url for price ascending`() {

        // GIVEN
        enqueueResponse(HttpURLConnection.HTTP_OK)

        // WHEN
        api
            .getPropertyResponse("pa")
            .blockingGet()

        val request = mockWebServer.takeRequest()

        // THEN
        Truth.assertThat(request.path).isEqualTo("/mobileapi/search?ob=pa")
    }

    @Test
    fun `Request should have correct url for bed descending`() {

        // GIVEN
        enqueueResponse(HttpURLConnection.HTTP_OK)

        // WHEN
        api
            .getPropertyResponse("bd")
            .blockingGet()

        val request = mockWebServer.takeRequest()

        // THEN
        Truth.assertThat(request.path).isEqualTo("/mobileapi/search?ob=bd")
    }

    @Test
    fun `Request should have correct url for correct page number`() {

        // GIVEN
        enqueueResponse(HttpURLConnection.HTTP_OK)

        // WHEN
        api
            .getPropertyResponseForPage(2)
            .blockingGet()

        val request = mockWebServer.takeRequest()

        // THEN
        Truth.assertThat(request.path).isEqualTo("/mobileapi/search?page=2&ob=")
    }

    @Test
    fun `Request should have correct url for correct page number with price descending`() {

        // GIVEN
        enqueueResponse(HttpURLConnection.HTTP_OK)

        // WHEN
        api
            .getPropertyResponseForPage(2, "pd")
            .blockingGet()

        val request = mockWebServer.takeRequest()

        // THEN
        Truth.assertThat(request.path).isEqualTo("/mobileapi/search?page=2&ob=pd")
    }

    @Test
    fun `When page not found should return 404 error`() {

        // GIVEN

        mockWebServer.dispatcher = QueueDispatcher()
        enqueueResponse(HttpURLConnection.HTTP_NOT_FOUND)
        val testObserver = TestObserver<PropertyResponse>()

        // WHEN
        api.getPropertyResponse().blockingSubscribe(testObserver)

        // THEN
        // Assert that throws HttpException
        testObserver.assertError(HttpException::class.java)

        // Assert that onComplete not called
        testObserver.assertNotComplete()

        testObserver.dispose()
    }

    /*
       HttpException is wrapped in RuntimeException by RxJava
     */
    @Test
    fun `Server down should return 500 error`() {

        // GIVEN
        mockWebServer.dispatcher = QueueDispatcher()
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        // WHEN
        val exception: RuntimeException =
            Assertions.assertThrows(
                RuntimeException::class.java
            ) {
                api
                    .getPropertyResponse()
                    .blockingGet()
            }

        // THEN
        Truth.assertThat(exception).isInstanceOf(HttpException::class.java)
        Truth.assertThat(exception.message).isEqualTo("HTTP 500 Server Error")
    }

    @Test
    fun `Service should return property list`() {

        // GIVEN
        val propertyDTO = propertyListOrderByNone

        // WHEN
        val expected = api
            .getPropertyResponse()
            .blockingGet()
            .res

        // THEN
        Truth.assertThat(expected.size).isEqualTo(propertyDTO.size)
        Truth.assertThat(expected).containsExactlyElementsIn(propertyDTO)
    }

    /**
     * ✅ This test PASSES when [TestObserver] used with [TestObserver.await]
     */
    @Test
    fun `Service should return property list with TestObserver`() {

        // GIVEN
        val propertyDTOList = propertyListOrderByNone

        // WHEN
        val testObserver = api
            .getPropertyResponse()
            .test()
            .await()
            // THEN
            .assertComplete()
            .assertNoErrors()
            .assertValue {
                it.res.size == propertyDTOList.size
            }

        testObserver.dispose()
    }

    @Test
    fun `Service should return property list with TestObserver blockingSubscribe`() {

        // GIVEN
        val propertyDTOList = propertyListOrderByNone
        val testObserver = TestObserver<PropertyResponse>()

        // WHEN
        api.getPropertyResponse().blockingSubscribe(testObserver)
        val expected = testObserver.values()[0].res

        // THEN
        Truth.assertThat(expected.size).isEqualTo(propertyDTOList.size)
        Truth.assertThat(expected).containsExactlyElementsIn(propertyDTOList)
        testObserver.dispose()
    }

    @Test
    fun `Service should return property list ordered by price ascening`() {

        // GIVEN
        val propertyDTOList = propertyListOrderByPriceAscending
        val testObserver = TestObserver<PropertyResponse>()

        // WHEN
        api.getPropertyResponse("pa").blockingSubscribe(testObserver)
        val expected = testObserver.values()[0].res

        // THEN
        Truth.assertThat(expected.size).isEqualTo(propertyDTOList.size)
        Truth.assertThat(expected).containsExactlyElementsIn(propertyDTOList)
        testObserver.dispose()
    }

    @Test
    fun `Service should return property list ordered by beds descending`() {

        // GIVEN
        val propertyDTOList = propertyListOrderByBedsDescending
        val testObserver = TestObserver<PropertyResponse>()

        // WHEN
        api.getPropertyResponse("bd").blockingSubscribe(testObserver)
        val expected = testObserver.values()[0].res

        // THEN
        Truth.assertThat(expected.size).isEqualTo(propertyDTOList.size)
        Truth.assertThat(expected).containsExactlyElementsIn(propertyDTOList)
        testObserver.dispose()
    }

    @Test
    fun `Service should return results for page2 after page1`() {

        // GIVEN
        val propertyDTOList1 = propertyListPage1
        val propertyDTOList2 = propertyListPage2

        val testObserver = TestObserver<PropertyResponse>()

        // WHEN
        api.getPropertyResponseForPage(1).blockingSubscribe(testObserver)
        val expected1 = testObserver.values()[0].res

        api.getPropertyResponseForPage(2).blockingSubscribe(testObserver)
        val expected2 = testObserver.values()[0].res

        // THEN
        Truth.assertThat(expected1.size).isEqualTo(propertyDTOList1.size)
        Truth.assertThat(expected1).containsExactlyElementsIn(propertyDTOList1)

        Truth.assertThat(expected2.size).isEqualTo(propertyDTOList2.size)
        Truth.assertThat(expected2).containsExactlyElementsIn(propertyDTOList2)

        testObserver.dispose()
    }

    @BeforeEach
    override fun setUp() {
        super.setUp()

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(PropertyApiRxJava::class.java)
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }
}
