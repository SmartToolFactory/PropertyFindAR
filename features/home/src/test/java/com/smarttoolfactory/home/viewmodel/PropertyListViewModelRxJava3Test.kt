package com.smarttoolfactory.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.domain.model.PropertyItem
import com.smarttoolfactory.domain.usecase.GetPropertiesUseCaseRxJava3
import com.smarttoolfactory.home.viewmodel.AbstractPropertyListVM.Companion.ORDER_BY_NONE
import com.smarttoolfactory.test_utils.RESPONSE_JSON_PATH
import com.smarttoolfactory.test_utils.rule.RxImmediateSchedulerRule
import com.smarttoolfactory.test_utils.test_observer.test
import com.smarttoolfactory.test_utils.util.convertToObjectFromJson
import com.smarttoolfactory.test_utils.util.getResourceAsText
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyListViewModelRxJava3Test {

    // Run tasks synchronously
    /**
     * Not using this causes java.lang.RuntimeException: Method getMainLooper in android.os.Looper
     * not mocked when `this.observeForever(observer)` is called
     */
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    /**
     * Changes schedulers of this test to specified [Scheduler]
     * Without this rule [Observable.observeOn] with AndroidSchedulers.mainThread
     * returns ExceptionInInitializerError
     */
    @Rule
    @JvmField
    val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

    companion object {

        private data class PropertyItems(
            val total: Int,
            val res: List<PropertyItem>
        )

        private val itemList = convertToObjectFromJson<PropertyItems>(
            getResourceAsText(RESPONSE_JSON_PATH)
        )!!.res
    }

    /*
        Mocks
     */
    private val useCase: GetPropertiesUseCaseRxJava3 = mockk()

    /**
     * ViewModel to test list which is SUT
     */
    private lateinit var viewModel: PropertyListViewModelRxJava3

    @Test
    fun `given exception returned from useCase, should have ViewState ERROR offlineFirst`() {

        // GIVEN
        every {
            useCase.getPropertiesOfflineFirst(ORDER_BY_NONE)
        } returns Single.error(Exception("Network Exception"))

        val testObserver = viewModel.propertyListViewState.test()

        // WHEN
        viewModel.getPropertyList()

        // THEN
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
        verify(exactly = 1) { useCase.getPropertiesOfflineFirst(ORDER_BY_NONE) }
    }

    @Test
    fun `given useCase fetched data, should have ViewState SUCCESS and data offlineFirst`() {

        // GIVEN
        every { useCase.getPropertiesOfflineFirst(ORDER_BY_NONE) } returns Single.just(itemList)

        val testObserver = viewModel.propertyListViewState.test()

        // WHEN
        viewModel.getPropertyList()

        // THEN
        testObserver
            .assertValue { states ->
                (
                    states[0].status == Status.LOADING &&
                        states[1].status == Status.SUCCESS
                    )
            }

        val finalState = testObserver.values()[1]
        val actual = finalState.data
        Truth.assertThat(actual?.size).isEqualTo(itemList.size)
        verify(exactly = 1) { useCase.getPropertiesOfflineFirst(ORDER_BY_NONE) }
        testObserver.dispose()
    }

    @Test
    fun `given exception returned from useCase, should have ViewState ERROR offlineLast`() {

        // GIVEN
        every {
            useCase.getPropertiesOfflineLast(ORDER_BY_NONE)
        } returns Single.error(Exception("Network Exception"))

        val testObserver = viewModel.propertyListViewState.test()

        // WHEN
        viewModel.refreshPropertyList()

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

    @Test
    fun `given useCase fetched data, should have ViewState SUCCESS and data offlineLast`() {

        // GIVEN
        every { useCase.getPropertiesOfflineLast(ORDER_BY_NONE) } returns Single.just(itemList)

        val testObserver = viewModel.propertyListViewState.test()

        // WHEN
        viewModel.refreshPropertyList()

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
            PropertyListViewModelRxJava3(useCase)
    }

    @After
    fun tearDown() {
        clearMocks(useCase)
    }
}
