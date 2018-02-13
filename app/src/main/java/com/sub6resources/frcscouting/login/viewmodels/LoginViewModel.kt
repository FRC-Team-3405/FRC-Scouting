package com.sub6resources.frcscouting.login.viewmodels

import accounts.TokenOuterClass
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.login.Login
import com.sub6resources.frcscouting.login.LoginRepository
import com.sub6resources.frcscouting.login.model.UserDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by whitaker on 12/30/17.
 */
class LoginViewModel: BaseViewModel() {

    val loginRepository by inject<LoginRepository>()
    val userDao by inject<UserDao>()
    val offlineUsers = Transformations.map(userDao.getUsers()) {it}
    val login = MutableLiveData<Login>()
    val user = Transformations.switchMap(login) {loginRepository.signInGrpc(it)}

    fun signIn(username: String, password: String): Boolean {
        if(username.isBlank() || password.isBlank())
            return false
        login.value = Login(username, password)
        return true
    }
}