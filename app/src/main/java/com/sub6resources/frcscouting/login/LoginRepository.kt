package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi, val userDao: UserDao) {

    /*fun signIn(login: Login): LiveData<BasicNetworkState<User>> = makeNetworkRequest(loginApi.signIn(login), userDao.signIn(login.username)) {
        insert { userDao.create(it.apply { username = login.username }) }
    }*/

    fun signIn(login: Login): LiveData<BasicNetworkState<User>> = makeNetworkRequest(loginApi.signIn(login), userDao.signIn(login.username)) {
        insert { userDao.create(it.apply { username = login.username })}
    }
}