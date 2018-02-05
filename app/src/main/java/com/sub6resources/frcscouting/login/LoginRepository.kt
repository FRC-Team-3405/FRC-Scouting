package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import com.sub6resources.frcscouting.login.model.User
import com.sub6resources.frcscouting.login.model.UserDao

//import com.sub6resources.frcscouting.protobuf.accounts.*

/**
 * Created by whitaker on 1/8/18.
 */
class LoginRepository(val loginApi: LoginApi, val userDao: UserDao) {

    fun signIn(login: Login): LiveData<BasicNetworkState<User>> = makeNetworkRequest(loginApi.signIn(login), userDao.signIn(login.username)) {
        insert { userDao.create(it.apply { username = login.username })}
    }

    fun doStuffWithUser() {
        //val mChannel = ManagedChannelBuilder.forAddress("10.0.2.2", 8080).usePlaintext(true).build()
        //val blockingStub = AccountsServiceGrpc.newBlockingStub(mChannel);
        //val asyncStub = AccountsServiceProto.newStub(mChannel);
    }
    val user by lazy {
//        UserProto.User.newBuilder().apply {
//            id = ""
//            username = "Test"
//            password = "T3st"
//            firstName = "Test"
//            lastName = "User"
//            emailAddress = "testuser@example.com"
//        }.build()
    }
}