package com.sub6resources.frcscouting.login.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sub6resources.frcscouting.login.*
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by whitaker on 12/30/17.
 */
class LoginViewModel: BaseViewModel() {

    val loginRepository: LoginRepository by inject<LoginRepository>()

    fun signIn(username: String, password: String): LiveData<LoginResult> {
        return loginRepository.signIn(Login(username, password))
    }
}