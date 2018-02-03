package com.sub6resources.frcscouting

import com.sub6resources.frcscouting.database.databaseModule
import com.sub6resources.frcscouting.login.LoginApi
import com.sub6resources.frcscouting.login.LoginRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by whitaker on 1/6/18.
 */

val appModule: Module = applicationContext {
    val retrofit = Retrofit.Builder().apply {
        baseUrl("http://robot.ryanberger.me")
        client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }.build()

    provide { LoginRepository(get(), get()) }
    provide { getLoginApi(retrofit) }
}

fun getLoginApi(retrofit: Retrofit): LoginApi {
    return retrofit.create(LoginApi::class.java)
}