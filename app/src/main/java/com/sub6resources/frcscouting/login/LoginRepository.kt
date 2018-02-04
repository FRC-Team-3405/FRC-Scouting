package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import io.grpc.ManagedChannelBuilder

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi, val userDao: UserDao) {

    fun signIn(login: Login): LiveData<BasicNetworkState<User>> = makeNetworkRequest(loginApi.signIn(login), userDao.signIn(login.username)) {
        insert { userDao.create(it.apply { username = login.username })}
    }


    val channel by lazy {
        ManagedChannelBuilder.forAddress("10.0.2.2", 8080)
                .usePlaintext(true)
                .build()
    }
    val stub by lazy {

    }
}