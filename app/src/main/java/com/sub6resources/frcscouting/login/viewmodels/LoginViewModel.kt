package com.sub6resources.frcscouting.login.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sub6resources.frcscouting.login.*
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by whitaker on 12/30/17.
 */
class LoginViewModel: BaseViewModel() {

    val loginRepository by inject<LoginRepository>()
    val userDao by inject<UserDao>()

    fun signIn(username: String, password: String): LiveData<LoginResult> {
        if(username.isBlank()) {
            return MutableLiveData<LoginResult>().apply {
                value = LoginFailure("Username is required")
            }
        }
        if(password.isBlank()) {
            return MutableLiveData<LoginResult>().apply {
                value = LoginFailure("Password is required")
            }
        }
        return loginRepository.signIn(Login(username, password))
    }

    fun getOfflineUsers(): LiveData<List<User>> {
        return userDao.getUsers()
    }
}