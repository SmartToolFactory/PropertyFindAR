package com.smarttoolfactory.core.viewstate

class ViewState<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {

    fun isSuccess() = status == Status.SUCCESS

    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null && status == Status.ERROR
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}
