package com.smarttoolfactory.domain.error

/**
 * Exception for indicating there is no data retrieved from remote or local source
 */
class EmptyDataException(message: String) : Exception(message)
