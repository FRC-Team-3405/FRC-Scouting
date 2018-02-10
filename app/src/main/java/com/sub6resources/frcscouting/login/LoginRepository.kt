package com.sub6resources.frcscouting.login

import accounts.AccountsServiceGrpc
import accounts.TokenOuterClass
import accounts.UserOuterClass
import android.arch.lifecycle.LiveData
import android.util.Log
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

    fun doStuffWithUser(): TokenOuterClass.Token {
        val mChannel = ManagedChannelBuilder.forAddress("10.0.2.2", 8080).usePlaintext(true).build()
        val blockingStub = AccountsServiceGrpc.newBlockingStub(mChannel)
        val asyncStub = AccountsServiceGrpc.newStub(mChannel)

        try {
            return blockingStub.authenticate(user)
        } catch(e: io.grpc.StatusRuntimeException) {
            Log.e("GRPC Error", "${e.status.code.value()} : " + e.trailers.toString(), e)
        } finally {
            return TokenOuterClass.Token.newBuilder().build()
        }
    }
    val user by lazy {
        UserOuterClass.User.newBuilder().apply {
            id = "7be58aaf-c844-4ee4-a449-8c917e8433e6" //Was 7
            username = "TestUser" //Was just Test
            password = "T3st"
            firstName = "Test"
            lastName = "User"
            emailAddress = "testuser@example.com"
        }.build()
    }
}