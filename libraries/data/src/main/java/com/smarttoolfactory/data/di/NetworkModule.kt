package com.smarttoolfactory.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.smarttoolfactory.data.api.PropertyApiCoroutines
import com.smarttoolfactory.data.api.PropertyApiRxJava
import com.smarttoolfactory.data.constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    companion object {
        private const val CLIENT_TIME_OUT = 120L
    }

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        chuckInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val okHttpBuilder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        okHttpBuilder
            .addInterceptor(chuckInterceptor)
            .connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun providePropertyApiCoroutines(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): PropertyApiCoroutines {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
        return retrofitBuilder.build().create(PropertyApiCoroutines::class.java)
    }

    @Singleton
    @Provides
    fun providePropertyApiRxJava3(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): PropertyApiRxJava {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
        return retrofitBuilder.build().create(PropertyApiRxJava::class.java)
    }

    @Singleton
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor(context = context)
}
