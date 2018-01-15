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

    val mediatorLiveData = MediatorLiveData<LoginResult>()

    fun signIn(login: Login): LiveData<LoginResult> {
        val dbLogin = signInDatabase(login)
        mediatorLiveData.addSource(dbLogin, { data ->
            mediatorLiveData.removeSource(dbLogin)
            if(data is LoginFailure) {
                val apiLogin = signInNetwork(login)
                mediatorLiveData.addSource(apiLogin, {
                    when (it) {
                        is LoginSuccess -> {
                            mediatorLiveData.removeSource(apiLogin)
                            createNewUserInDatabase(it.user, login.username)
                            mediatorLiveData.addSource(signInDatabase(login), {
                                mediatorLiveData.value = it
                            })
                        }
                        is LoginFailure -> {
                            mediatorLiveData.value = it
                        }
                    }
                })
            } else {
                mediatorLiveData.addSource(dbLogin, {
                    mediatorLiveData.value = it
                })
            }
        })
        return mediatorLiveData
    }

    private fun createNewUserInDatabase(user: User, _username: String) {
        user.apply {
            username = _username
            id = UUID.randomUUID()
        }
        userDao.create(user)
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

    fun signInDatabase(login: Login): LiveData<LoginResult> {
        val result = MutableLiveData<LoginResult>()
        val user: User? = userDao.signIn(login.username)
        if(user != null)
            result.value = LoginSuccess(user)
        else
            result.value = LoginFailure("UserDoesNotExistInLocalDatabase")
        return result
    }
}


//Sealed class for the win
sealed class LoginResult
data class LoginSuccess(val user: User): LoginResult()
data class LoginFailure(val error: String): LoginResult()