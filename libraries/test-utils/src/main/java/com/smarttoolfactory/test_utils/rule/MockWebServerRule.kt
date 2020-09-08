package com.smarttoolfactory.test_utils.rule

import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule for JUnit4 to invoke actions which are
 * start [MockWebServer],
 * run the test ,
 * and shut  [MockWebServer] down after the test is run.
 */
class MockWebServerRule : TestRule {

    val mockWebServer = MockWebServer()

    override fun apply(
        base: Statement,
        description: Description
    ): Statement {

        return object : Statement() {

            @Throws(Throwable::class)
            override fun evaluate() {
                mockWebServer.start()
                base.evaluate()
                mockWebServer.shutdown()
            }
        }
    }
}
