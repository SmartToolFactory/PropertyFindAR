package com.smarttoolfactory.data.api

import com.google.common.truth.Truth
import com.smarttoolfactory.data.model.remote.PropertyDTO
import com.smarttoolfactory.test_utils.extension.TestCoroutineExtension
import java.net.HttpURLConnection
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PropertyApiCoroutinesTest : AbstractPropertyApiTest() {

    companion object {
        @JvmField
        @RegisterExtension
        val testCoroutineExtension = TestCoroutineExtension()

        val testCoroutineScope = testCoroutineExtension.testCoroutineScope
    }

    /**
     * Api is the SUT to test headers, url, response and DTO objects
     */
    private lateinit var api: PropertyApiCoroutines

    /**
     * ❌ This test FAILS, with launch builder sometimes fail, sometimes PASS
     */
    @Test
    fun `Given a valid request, should be done to correct url`() =
        testCoroutineExtension.runBlockingTest {

            // GIVEN
            enqueueResponse(HttpURLConnection.HTTP_OK)

            launch(this.coroutineContext) {
                api.getPropertyResponse()
            }
            advanceUntilIdle()

//            launch(testCoroutineScope.coroutineContext) {
            val request = mockWebServer.takeRequest()

            // THEN
            Truth.assertThat(request.path).isEqualTo("mobileapi/search?ob=")
        }

    /**
     * ❌ This test FAILS, with launch builder sometimes fail, sometimes PASS
     */
    @Test
    fun `Given api returns 200, should have list of properties`() =
        testCoroutineExtension.runBlockingTest {

            // GIVEN
            enqueueResponse(HttpURLConnection.HTTP_OK)

            // WHEN
            var postList: List<PropertyDTO> = emptyList()

            launch(this.coroutineContext) {
                postList = api.getPropertyResponse().res
            }
            advanceUntilIdle()

            // THEN
            Truth.assertThat(postList).isNotNull()
            Truth.assertThat(postList?.size).isEqualTo(25)
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
            .build()
            .create(PropertyApiCoroutines::class.java)
    }

    @AfterEach
    override fun tearDown() {
        super.tearDown()
    }
}
