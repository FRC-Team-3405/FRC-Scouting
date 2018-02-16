package com.sub6resources.frcscouting.login.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.sub6resources.frcscouting.login.Login
import com.sub6resources.frcscouting.login.LoginRepository
import com.sub6resources.frcscouting.login.UserMessage
import com.sub6resources.frcscouting.login.model.UserDao
import com.sub6resources.utilities.BaseViewModel
import org.koin.standalone.inject

/**
 * Created by 59485 on 2/15/18.
 */
class SignUpViewModel: BaseViewModel() {

    val loginRepository by inject<LoginRepository>()
    val userDao by inject<UserDao>()
    val offlineUsers = Transformations.map(userDao.getUsers()) {it}
    val userMessage = MutableLiveData<UserMessage>()
    val createdUser = Transformations.switchMap(userMessage) {loginRepository.signUpGrpc(it)}

    fun signUp(_username: String, _password: String, _firstName: String, _lastName: String, _emailAddress: String): Boolean {
        if(_username.isBlank() || _password.isBlank() || _firstName.isBlank() || _lastName.isBlank() || _emailAddress.isBlank())
            return false
        userMessage.value = UserMessage.newBuilder().apply {
            username = _username
            password = _password
            firstName = _firstName
            lastName = _lastName
            emailAddress = _emailAddress
        }.build()
        return true
    }
}