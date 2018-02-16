package com.sub6resources.frcscouting.login

import accounts.AccountsServiceGrpc
import accounts.TokenOuterClass
import accounts.UserOuterClass
import android.arch.lifecycle.LiveData
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao
import io.grpc.ManagedChannel

        /**
 * Created by whitaker on 1/8/18.
 */

typealias UserMessage = UserOuterClass.User
typealias TokenMessage = TokenOuterClass.Token


class LoginRepository(val channel: ManagedChannel, val userDao: UserDao) {

    val asyncStub by lazy { AccountsServiceGrpc.newStub(channel) }

    fun signInGrpc(login: Login): LiveData<BasicNetworkState<TokenMessage>> {

        val message = UserMessage.newBuilder().apply {
            username = login.username
            password = login.password
        }.build()
        // TADA
        return makeGrpcCall<UserMessage, TokenMessage>(channel, curry(asyncStub::authenticate)(message), userDao.signIn(login.username)) {
            onError {
                BasicNetworkState.Error(it.localizedMessage ?: "Unknown error")
            }
            insert {
                userDao.create(User(message.username, it.generatedToken))
            }
        }
    }

    fun signUpGrpc(userMessage: UserMessage): LiveData<BasicNetworkState<UserMessage>> {

        return makeGrpcCall<UserMessage, UserMessage>(channel, curry(asyncStub::createUser)(userMessage)) {
            onError {
                BasicNetworkState.Error(it.localizedMessage ?: "Unknown error")
            }
        }

    }
}