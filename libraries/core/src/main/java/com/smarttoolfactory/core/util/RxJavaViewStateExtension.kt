package com.smarttoolfactory.core.util

import com.smarttoolfactory.core.viewstate.Status
import com.smarttoolfactory.core.viewstate.ViewState
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

fun <T> Observable<T>.convertToObservableViewState(): Observable<ViewState<T>> {
    return this
        .map { data ->
            ViewState(status = Status.SUCCESS, data = data)
        }
        .onErrorResumeNext { throwable: Throwable ->
            Observable.just(ViewState(status = Status.ERROR, error = throwable))
        }
}

fun <T> Observable<T>.convertToObservableViewStateWithLoading(): Observable<ViewState<T>> {
    return this
        .map { data ->
            ViewState(status = Status.SUCCESS, data = data)
        }
        .onErrorResumeNext { throwable: Throwable ->
            Observable.just(ViewState(status = Status.ERROR, error = throwable))
        }
        .startWith(Observable.just(ViewState(status = Status.LOADING)))
}

fun <T> Single<T>.convertFromSingleToObservableViewState(): Observable<ViewState<T>> {
    return this
        .toObservable()
        .convertToObservableViewState()
}

fun <T> Single<T>.convertFromSingleToObservableViewStateWithLoading(): Observable<ViewState<T>> {
    return this
        .toObservable()
        .convertToObservableViewStateWithLoading()
}

fun <T> Single<T>.convertToSingleViewState(): Single<ViewState<T>>? {
    return this
        .map { data ->
            ViewState(status = Status.SUCCESS, data = data)
        }
        .onErrorResumeNext { throwable: Throwable ->
            Single.just(ViewState(status = Status.ERROR, error = throwable))
        }
}

fun <T> Single<T>.convertFromSingletToFlowableViewState(): Flowable<ViewState<T>>? {
    return this
        .map { data ->
            ViewState(status = Status.SUCCESS, data = data)
        }
        .onErrorResumeNext { throwable: Throwable ->
            Single.just(ViewState(status = Status.ERROR, error = throwable))
        }
        .startWith(Single.just(ViewState(status = Status.LOADING)))
}
