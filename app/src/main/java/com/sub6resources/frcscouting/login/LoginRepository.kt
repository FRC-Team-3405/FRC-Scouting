package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi) {
    fun signIn(login: Login): LiveData<LoginResult> {
        val liveData: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()
        loginApi.signIn(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ user ->
            liveData.value = LoginSuccess(user)
        }, { throwable ->
            liveData.value = LoginFailure(throwable.message ?: throwable.toString())
        })
        return liveData
    }
}

//Sealed class for the win
sealed class LoginResult
data class LoginSuccess(val user: User): LoginResult()
data class LoginFailure(val error: String): LoginResult()