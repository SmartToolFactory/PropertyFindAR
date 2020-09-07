package com.smarttoolfactory.core.util

import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.convertToFlowViewState(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
): Flow<ViewState<T>> {
    return this
        .map { postList -> ViewState(status = Status.SUCCESS, data = postList) }
        .catch { cause: Throwable -> emitAll(flowOf(ViewState(Status.ERROR, error = cause))) }
        .flowOn(dispatcher)
}
