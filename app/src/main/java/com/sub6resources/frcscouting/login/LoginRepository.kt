package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.sub6resources.frcscouting.login.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi) {
    fun signIn(login: Login): LiveData<LoginResult> {
        val mediatorLiveData = MediatorLiveData<LoginResult>()
        mediatorLiveData.addSource(signInNetwork(login), {
            if(it is LoginSuccess) {

            }
            mediatorLiveData.value = it
        })
        return mediatorLiveData
    }

    fun signInNetwork(login: Login): LiveData<LoginResult> {
        val liveData: MutableLiveData<LoginResult> = MutableLiveData<LoginResult>()
        loginApi.signIn(login).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ user ->
            liveData.value = LoginSuccess(user)
        }, { throwable ->
            liveData.value = LoginFailure(throwable.message ?: throwable.toString())
        })
        return liveData
    }

    /*fun signInDatabase(login: Login): LiveData<LoginResult> {
        return Transformations.switchMap(database.userDao.signIn(login.username, login.password)) { user ->
            return@switchMap MutableLiveData<LoginResult>().apply {value = LoginSuccess(user)}
        }
    }*/
}


//Sealed class for the win
sealed class LoginResult
data class LoginSuccess(val user: User): LoginResult()
data class LoginFailure(val error: String): LoginResult()