package com.smarttoolfactory.domain.dispatcher

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Class for providing dispatcher for different thread operations. This class is useful
 * in tests to control every thread in one place.
 *
 *  Multiple flowOn operators fuse to a single flowOn with a combined context.
 *  The elements of the context of the first flowOn operator naturally take precedence over
 *
 *  the elements of the second flowOn operator when they have the same context keys, for example:
 *  ```
 * flow.map { ... } // Will be executed in IO
 * .flowOn(Dispatchers.IO) // This one takes precedence
 * .flowOn(Dispatchers.Default)
 * ```
 *
 * ### Note: While [Observable.observeOn] runs down stream, [Flow.flowOn] works upstream
 */

data class UseCaseDispatchers(
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
)
