package com.smarttoolfactory.test_utils.extension

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.util.concurrent.Executor
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
class RxImmediateSchedulerExtension : BeforeEachCallback, AfterEachCallback {

    private val immediate = object : Scheduler() {

        override fun createWorker(): Worker {
            return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true, true)
        }
    }

//    private val immediate = Schedulers.trampoline()

    override fun beforeEach(context: ExtensionContext?) {
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    override fun afterEach(context: ExtensionContext?) {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}
