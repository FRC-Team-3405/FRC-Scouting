package com.sub6resources.frcscouting.login.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
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
    val offlineUsers = Transformations.map(userDao.getUsers()) {it}
    val login = MutableLiveData<Login>()
    val user = Transformations.switchMap(login) {loginRepository.signIn(it)}

    fun signIn(username: String, password: String): Boolean {
        if(username.isBlank() || password.isBlank())
            return false
        login.value = Login(username, password)
        return true
    }
}