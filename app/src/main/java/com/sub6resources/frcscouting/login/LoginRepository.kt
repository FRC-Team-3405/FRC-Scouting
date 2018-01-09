package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sub6resources.frcscouting.database.AppDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi) {
    fun signIn(login: Login): LiveData<LoginResult> {
        val liveData: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()
        loginApi.signIn(login).enqueue(object: Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                liveData.value = LoginFailure(t?.message ?: "Unknown Error")
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.isSuccessful == true) {
                    response.body().let { body ->
                        if(body!!.has("token")){
                            val token = body.get("token").asString
                            liveData.value = LoginSuccess(login.username, token)
                        }
                    }
                } else {
                    response?.errorBody()?.let { errorBody ->
                        val error = Gson().fromJson(errorBody.string(), LoginError::class.java)
                        liveData.value = LoginFailure(error.non_field_errors[0])
                    }
                }
            }
        })
        return liveData
    }
}

//Sealed class for the win
sealed class LoginResult
data class LoginSuccess(val username: String, val token: String): LoginResult()
data class LoginFailure(val error: String): LoginResult()