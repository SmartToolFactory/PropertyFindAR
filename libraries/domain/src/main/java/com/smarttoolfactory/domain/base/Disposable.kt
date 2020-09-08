package com.smarttoolfactory.domain.base

/**
 * Interface for adding common behavior for any class that has items that need to be disposed
 * such as RxJava Observables or Coroutines jobs.
 */
interface Disposable {

    fun dispose()
}
