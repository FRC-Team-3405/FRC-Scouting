package com.sub6resources.frcscouting.login.viewmodels

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sub6resources.frcscouting.login.*
import com.sub6resources.utilities.BaseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by whitaker on 12/30/17.
 */
class LoginViewModel: BaseViewModel() {

    fun signIn(username: String, password: String, onResult: (success: LoginResult) -> Unit) {
        LoginApi().api.signIn(Login(username, password)).enqueue(object: Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                onResult(LoginFailure(t?.message ?: "Unknown Error"))
            }

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                if (response?.isSuccessful == true) {
                    response.body().let { body ->
                        if(body!!.has("token")){
                            val token = body.get("token").asString
                            onResult(LoginSuccess(username, token))
                        }
                    }
                } else {
                    response?.errorBody()?.let { errorBody ->
                        val error = Gson().fromJson(errorBody.string(), LoginError::class.java)
                        onResult(LoginFailure(error.non_field_errors[0]))
                    }
                }
            }
        })
    }
}