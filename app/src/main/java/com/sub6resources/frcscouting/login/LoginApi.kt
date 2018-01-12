package com.sub6resources.frcscouting.login

import com.sub6resources.frcscouting.login.model.User
import io.reactivex.Single
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