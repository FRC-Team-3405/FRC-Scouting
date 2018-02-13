package com.sub6resources.frcscouting.login

import accounts.AccountsServiceGrpc
import accounts.TokenOuterClass
import accounts.UserOuterClass
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi, val userDao: UserDao) {

    val mChannel by lazy {ManagedChannelBuilder.forAddress("10.0.2.2", 8080).usePlaintext(true).build()}

    fun signIn(login: Login): LiveData<BasicNetworkState<User>> = makeNetworkRequest(loginApi.signIn(login), userDao.signIn(login.username)) {
        insert { userDao.create(it.apply { username = login.username })}
    }

    fun signInGrpc(login: Login): LiveData<BasicNetworkState<User>> {
        val asyncStub = AccountsServiceGrpc.newStub(mChannel)
        val mediator = MediatorLiveData<BasicNetworkState<User>>()

        mediator.value = BasicNetworkState.Loading()

        check(asyncStub::authenticate)
        asyncStub.authenticate(UserOuterClass.User.newBuilder().apply {
            username = login.username
            password = login.password
        }.build(), object: StreamObserver<TokenOuterClass.Token> {
            override fun onNext(value: TokenOuterClass.Token?) {
                userDao.create(User().apply {username = login.username; token=value!!.generatedToken})
                mediator.addSource(userDao.signIn(login.username)) { mediator.postValue(BasicNetworkState.Success(it!!)) }
            }
            override fun onError(t: Throwable?) {
                Log.e("GRPC", "Error", t)
                mediator.postValue(BasicNetworkState.Error(t?.message!!))
            }
            override fun onCompleted() {}
        })
        return mediator
    }
}