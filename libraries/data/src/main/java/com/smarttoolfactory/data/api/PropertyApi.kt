package com.smarttoolfactory.data.api

import com.smarttoolfactory.data.constant.ORDER_BY_NONE
import com.smarttoolfactory.data.model.remote.PropertyResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PropertyApiCoroutines {

    @GET("mobileapi/search")
    suspend fun getPropertyResponse(
        @Query("ob") orderBy: String = ORDER_BY_NONE
    ): PropertyResponse

    @GET("mobileapi/search")
    suspend fun getPropertyResponseForPage(
        @Query("page") page: Int,
        @Query("ob") orderBy: String = ORDER_BY_NONE
    ): PropertyResponse
}

interface PropertyApiRxJava {

    @GET("mobileapi/search")
    fun getPropertyResponse(
        @Query("ob") orderBy: String = ORDER_BY_NONE
    ): Single<PropertyResponse>

    @GET("mobileapi/search")
    fun getPropertyResponseForPage(
        @Query("page") page: Int,
        @Query("ob") orderBy: String = ORDER_BY_NONE
    ): Single<PropertyResponse>
}
