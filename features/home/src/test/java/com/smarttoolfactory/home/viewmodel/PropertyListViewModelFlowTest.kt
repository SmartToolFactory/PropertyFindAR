package com.smarttoolfactory.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.domain.ORDER_BY_NONE
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.property.GetPropertiesUseCaseFlow
import com.smarttoolfactory.home.propertylist.flow.PropertyListViewModelFlow
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.rule.TestCoroutineRule
import com.smarttoolfactory.test_utils.test_observer.test
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * ❌ FIXME Either [LiveDataTestObserver] or Flow is bugged with tests, solve the issue
 */
class PropertyListViewModelFlowTest {

    // Run tasks synchronously
    /**
     * Not using this causes java.lang.RuntimeException: Method getMainLooper in android.os.Looper
     * not mocked when `this.observeForever(observer)` is called
     */
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * Rule for testing Coroutines with [TestCoroutineScope] and [TestCoroutineDispatcher]
     */
    @Rule
    @JvmField
    var testCoroutineRule = TestCoroutineRule()

    companion object {

        private data class PropertyItems(
            val total: Int,
            val res: List<PropertyItem>
        )

        private val itemList =
            convertToObjectFromJson<PropertyItems>(
                getResourceAsText(RESPONSE_JSON_PATH)
            )!!.res
    }

    /*
        Mocks
     */
    private val useCase: GetPropertiesUseCaseFlow = mockk()

    /**
     * ViewModel to test list which is SUT
     */
    private lateinit var viewModel: PropertyListViewModelFlow

    @Test
    fun `given exception returned from useCase, should have ViewState ERROR offlineFirst`() =
        testCoroutineRule.runBlockingTest {

            // GIVEN
            every {
                useCase.getPropertiesOfflineFirst()
            } returns flow<List<PropertyItem>> {
                emit(throw Exception("Network Exception"))
            }

            every {
                useCase.getCurrentSortKey()
            } returns flow {
                emit((ORDER_BY_NONE))
            }

            val testObserver = viewModel.propertyListViewState.test()

            // WHEN

            viewModel.getPropertyList()

            // THEN
            println("💀 THEN")
            testObserver
                .assertValue { states ->
                    (
                        states[0].status == Status.LOADING &&
                            states[1].status == Status.ERROR
                        )
                }

            val finalState = testObserver.values()[1]
            Truth.assertThat(finalState.error?.message).isEqualTo("Network Exception")
            Truth.assertThat(finalState.error).isInstanceOf(Exception::class.java)
            verify(atMost = 1) { useCase.getPropertiesOfflineFirst() }
        }

    @Test
    fun `given useCase fetched data, should have ViewState SUCCESS and data offlineFirst`() =
        testCoroutineRule.runBlockingTest {

            // GIVEN
            every { useCase.getPropertiesOfflineFirst() } returns flow {
                emit(itemList)
            }

            every {
                useCase.getCurrentSortKey()
            } returns flow<String> {
                emit((ORDER_BY_NONE))
            }

            val testObserver = viewModel.propertyListViewState.test()

            // WHEN
            viewModel.getPropertyList()

            // THEN
            println("💀 THEN")
            val viewStates = testObserver.values()
            Truth.assertThat(viewStates.first().status).isEqualTo(Status.LOADING)

            val actual = viewStates.last().data

            Truth.assertThat(actual?.size).isEqualTo(itemList.size)
            verify(exactly = 1) { useCase.getPropertiesOfflineFirst() }
            testObserver.dispose()
        }

    @Test
    fun `given exception returned from useCase, should have ViewState ERROR offlineLast`() =
        testCoroutineRule.runBlockingTest {

            // GIVEN
            every {
                useCase.getPropertiesOfflineLast(ORDER_BY_NONE)
            } returns flow<List<PropertyItem>> {
                emit(throw Exception("Network Exception"))
            }

            val testObserver = viewModel.propertyListViewState.test()

            // WHEN
            viewModel.refreshPropertyList()
            advanceUntilIdle()

            // THEN
            testObserver
                .assertValue { states ->
                    (
                        states[0].status == Status.LOADING &&
                            states[1].status == Status.ERROR
                        )
                }
                .dispose()

            val finalState = testObserver.values()[1]
            Truth.assertThat(finalState.error?.message).isEqualTo("Network Exception")
            Truth.assertThat(finalState.error).isInstanceOf(Exception::class.java)
            verify(atMost = 1) { useCase.getPropertiesOfflineLast(ORDER_BY_NONE) }
        }

    /**
     * ❌ FIXME This test is flaky, find out the cause, sometimes null is returned
     */
    @Test
    fun `given useCase fetched data, should have ViewState SUCCESS and data offlineLast`() =
        testCoroutineRule.runBlockingTest {

            // GIVEN
            every {
                useCase.getPropertiesOfflineLast(ORDER_BY_NONE)
            } returns flow {
                emit(itemList)
            }

            val testObserver = viewModel.propertyListViewState.test()

            // WHEN
            viewModel.refreshPropertyList()
            advanceUntilIdle()

            // THEN
            val viewStates = testObserver.values()
            Truth.assertThat(viewStates.first().status).isEqualTo(Status.LOADING)

            val actual = viewStates.last().data
            Truth.assertThat(actual?.size).isEqualTo(itemList.size)
            verify(exactly = 1) { useCase.getPropertiesOfflineLast(ORDER_BY_NONE) }
            testObserver.dispose()
        }

    @Before
    fun setUp() {
        viewModel =
            PropertyListViewModelFlow(testCoroutineRule.testCoroutineScope, useCase)
    }

    @After
    fun tearDown() {
        clearMocks(useCase)
    }
}
