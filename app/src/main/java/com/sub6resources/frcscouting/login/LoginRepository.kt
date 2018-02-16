package com.sub6resources.frcscouting.login

import accounts.AccountsServiceGrpc
import accounts.TokenOuterClass
import accounts.UserOuterClass
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.util.Log
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver

/**
 * Created by whitaker on 1/8/18.
 */

typealias UserMessage = UserOuterClass.User
typealias TokenMessage = TokenOuterClass.Token


class LoginRepository(val channel: ManagedChannel, val userDao: UserDao) {

    fun signInGrpc(login: Login): LiveData<BasicNetworkState<User>> {
        val asyncStub = AccountsServiceGrpc.newStub(channel)
        val mediator = MediatorLiveData<BasicNetworkState<User>>()

        mediator.value = BasicNetworkState.Loading()

        val message = UserMessage.newBuilder().apply {
            username = login.username
            password = login.password
        }.build()
        // TADA
        makeGrpcCall<UserMessage, TokenMessage>(channel, curry(asyncStub::authenticate)(message), userDao.signIn(login.username)) {
            onError {
                mediator.postValue(BasicNetworkState.Error(it.localizedMessage ?: "Unknown error"))
            }
            insert {
                userDao.create(User(it.userId, it.generatedToken))
            }
        }
        return mediator
    }
}