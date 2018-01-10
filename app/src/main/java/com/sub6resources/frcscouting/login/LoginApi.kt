package com.sub6resources.frcscouting.login

import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by whitaker on 12/30/17.
 */

interface LoginApi {
    @POST("api-token-auth/")
    fun signIn(@Body user: Login): Single<User>
}

data class Login(val username: String, val password: String)
data class User(val username: String, val token: String)