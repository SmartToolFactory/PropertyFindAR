package com.smarttoolfactory.test_utils.test_observer

import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class FlowTestObserver<T>(
    private val coroutineScope: CoroutineScope,
    private val flow: Flow<T>,
    private val waitForDelay: Boolean = false
) {
    private val testValues = mutableListOf<T>()
    private var error: Throwable? = null

    private var isInitialized = false

    private var isCompleted = false

    private lateinit var job: Job

    private suspend fun init() {
        job = createJob(coroutineScope)

        // Wait this job after end of possible delays
//        job.join()
    }

    private suspend fun initialize() {

        if (!isInitialized) {

            if (waitForDelay) {
                try {
                    withTimeout(Long.MAX_VALUE) {
                        job = createJob(this)
                    }
                } catch (e: Exception) {
                    isCompleted = false
                }
            } else {
                job = createJob(coroutineScope)
            }
        }
    }

    private fun createJob(scope: CoroutineScope): Job {

        val job = flow
            .onStart { isInitialized = true }
            .onCompletion { cause ->
                isCompleted = (cause == null)
            }
            .catch { throwable ->
                error = throwable
            }
            .onEach { testValues.add(it) }
            .launchIn(scope)
        return job
    }

    suspend fun assertNoValue(): FlowTestObserver<T> {

        initialize()

        if (testValues.isNotEmpty()) throw AssertionError(
            "Assertion error! Actual size ${testValues.size}"
        )
        return this
    }

    suspend fun assertValueCount(count: Int): FlowTestObserver<T> {

        initialize()

        if (count < 0) throw AssertionError(
            "Assertion error! Value count cannot be smaller than zero"
        )
        if (count != testValues.size) throw AssertionError(
            "Assertion error! Expected $count while actual ${testValues.size}"
        )
        return this
    }

    suspend fun assertValues(vararg values: T): FlowTestObserver<T> {

        initialize()

        if (!testValues.containsAll(values.asList()))
            throw AssertionError("Assertion error! At least one value does not match")
        return this
    }

    suspend fun assertValues(predicate: (List<T>) -> Boolean): FlowTestObserver<T> {

        initialize()

        if (!predicate(testValues))
            throw AssertionError("Assertion error! At least one value does not match")
        return this
    }

    /**
     * Asserts that this [FlowTestObserver] received exactly one [Flow.onEach] or [Flow.collect]
     * value for which the provided predicate returns `true`.
     */
    suspend fun assertValue(predicate: (T) -> Boolean): FlowTestObserver<T> {
        return assertValueAt(0, predicate)
    }

    suspend fun assertValueAt(index: Int, predicate: (T) -> Boolean): FlowTestObserver<T> {

        initialize()

        if (testValues.size == 0) throw AssertionError("Assertion error! No values")

        if (index < 0) throw AssertionError(
            "Assertion error! Index cannot be smaller than zero"
        )

        if (index > testValues.size) throw AssertionError(
            "Assertion error! Invalid index: $index"
        )

        if (!predicate(testValues[index]))
            throw AssertionError("Assertion error! At least one value does not match")

        return this
    }

    suspend fun assertValueAt(index: Int, value: T): FlowTestObserver<T> {

        initialize()

        if (testValues.size == 0) throw AssertionError("Assertion error! No values")

        if (index < 0) throw AssertionError(
            "Assertion error! Index cannot be smaller than zero"
        )

        if (index > testValues.size) throw AssertionError(
            "Assertion error! Invalid index: $index"
        )

        if (testValues[index] != value)
            throw AssertionError("Assertion Error Objects don't match")

        return this
    }

    /**
     * Asserts that this [FlowTestObserver] received
     * [Flow.catch] the exact same throwable. Since most exceptions don't implement `equals`
     * it would be better to call overload to test against the class of
     * an error instead of an instance of an error
     */
    suspend fun assertError(throwable: Throwable): FlowTestObserver<T> {

        initialize()

        val errorNotNull = exceptionNotNull()

        if (!(
            errorNotNull::class.java == throwable::class.java &&
                errorNotNull.message == throwable.message
            )
        )
            throw AssertionError(
                "Assertion Error! " +
                    "throwable: $throwable does not match $errorNotNull"
            )
        return this
    }

    /**
     * Asserts that this [FlowTestObserver] received
     * [Flow.catch] which is an instance of the specified errorClass Class.
     */
    suspend fun assertError(errorClass: Class<out Throwable>): FlowTestObserver<T> {

        initialize()

        val errorNotNull = exceptionNotNull()

        if (errorNotNull::class.java != errorClass)
            throw AssertionError(
                "Assertion Error! errorClass $errorClass" +
                    " does not match ${errorNotNull::class.java}"
            )
        return this
    }

    /**
     *  Asserts that this [FlowTestObserver] received exactly [Flow.catch]  event for which
     * the provided predicate returns `true`.
     */
    suspend fun assertError(predicate: (Throwable) -> Boolean): FlowTestObserver<T> {

        initialize()

        val errorNotNull = exceptionNotNull()

        if (!predicate(errorNotNull))
            throw AssertionError("Assertion Error! Exception for $errorNotNull")
        return this
    }

    suspend fun assertNoErrors(): FlowTestObserver<T> {

        initialize()

        if (error != null)
            throw AssertionError("Assertion Error! Exception occurred $error")

        return this
    }

    suspend fun assertNull(): FlowTestObserver<T> {

        initialize()

        testValues.forEach {
            if (it != null) throw AssertionError(
                "Assertion Error! " +
                    "There are more than one item that is not null"
            )
        }

        return this
    }

    /**
     * Assert that this [FlowTestObserver] received [Flow.onCompletion] event without a [Throwable]
     */
    suspend fun assertComplete(): FlowTestObserver<T> {

        initialize()

        if (!isCompleted) throw AssertionError(
            "Assertion Error!" +
                " Job is not completed or onCompletion called with a error!"
        )
        return this
    }

    /**
     * Assert that this [FlowTestObserver] either not received [Flow.onCompletion] event or
     * received event with
     */
    suspend fun assertNotComplete(): FlowTestObserver<T> {

        initialize()

        if (isCompleted) throw AssertionError("Assertion Error! Job is completed!")
        return this
    }

    suspend fun values(predicate: (List<T>) -> Unit): FlowTestObserver<T> {
        predicate(testValues)
        return this
    }

    suspend fun values(): List<T> {

        initialize()

        return testValues
    }

    private fun exceptionNotNull(): Throwable {

        if (error == null)
            throw AssertionError("There is no exception")

        return error!!
    }

    fun dispose() {
        job.cancel()
    }
}

/**
 * Creates a RxJava style test observer that uses [Flow.onStart], [Flow.onEach], [Flow.catch]
 * and [Flow.onCompletion] to check states and test values
 *
 * * Set waitForDelay true for testing delay.
 *
 * ###  Note: waiting for delay with a channel that sends values throw TimeoutCancellationException,
 * don't use timeout with channel
 * TODO Fix channel issue
 */
suspend fun <T> Flow<T>.test(
    scope: CoroutineScope,
    waitForDelay: Boolean = true
): FlowTestObserver<T> {

    return FlowTestObserver(scope, this@test, waitForDelay)
}

/**
 * Test function that awaits with time out until each delay method is run and then since
 * it takes a predicate that runs after a timeout.
 */
suspend fun <T> Flow<T>.testAfterDelay(
    scope: CoroutineScope,
    predicate: suspend FlowTestObserver<T>.() -> Unit

): Job {
    return scope.launch(coroutineContext) {
        FlowTestObserver(this, this@testAfterDelay, true).predicate()
    }
}
