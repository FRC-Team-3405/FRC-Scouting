package com.sub6resources.frcscouting

import com.sub6resources.frcscouting.login.LoginApi
import com.sub6resources.frcscouting.login.LoginRepository
import com.sub6resources.frcscouting.login.viewmodels.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.module.AndroidModule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by whitaker on 1/6/18.
 */
class AppModule(): AndroidModule() {

    val loginApi by lazy {
        Retrofit.Builder().apply {
            baseUrl("http://watch.ryanberger.me")
            client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }).build())
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(LoginApi::class.java)
    }

    override fun context() = applicationContext {
        provide { LoginRepository(loginApi) }
    }
}