package com.sub6resources.frcscouting

import com.sub6resources.frcscouting.login.LoginRepository
import io.grpc.ManagedChannelBuilder
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

/**
 * Created by whitaker on 1/6/18.
 */

val appModule: Module = applicationContext {
    provide {
        ManagedChannelBuilder
                .forAddress("10.0.2.2", 8080)
                .usePlaintext(true)
                .build()
    }
    provide { LoginRepository(get(), get()) }
}
