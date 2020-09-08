package com.smarttoolfactory.test_utils.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * LifeCycle
 *
 * * BeforeAllCallback
 * * BeforeAll
 * * BeforeEachCallback
 * * BeforeEach
 * * BeforeTestExecutionCallback
 * * Test
 * * AfterTestExecutionCallback
 * * AfterEach
 * * AfterEachCallback
 * * AfterAll
 * * AfterAllCallback
 */
class TestCoroutineExtension : BeforeEachCallback, AfterEachCallback {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun beforeEach(context: ExtensionContext?) {
        println("🚙 TestCoroutineExtension beforeEach()")
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {

        println("🚗 TestCoroutineExtension afterEach()")

        Dispatchers.resetMain()
        try {
            testCoroutineScope.cleanupTestCoroutines()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }
}
