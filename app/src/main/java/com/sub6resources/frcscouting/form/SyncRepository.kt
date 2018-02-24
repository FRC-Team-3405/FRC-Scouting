package com.sub6resources.frcscouting.form

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.google.protobuf.Empty
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.frcscouting.login.curry
import com.sub6resources.frcscouting.login.makeGrpcCall
import io.grpc.ManagedChannel
import stats.*

typealias FormSyncMessage = FormSyncOuterClass.FormSync
typealias FormMessage = FormOuterClass.Form
typealias FieldMessage = FieldOuterClass.Field
typealias ChoiceMessage = ChoiceOuterClass.Choice
typealias FormResponseMessage = FormResponseOuterClass.FormResponse
typealias FieldResponseMessage = FieldResponseOuterClass.FieldResponse

class SyncRepository(private val channel: ManagedChannel) {
    val asyncStub by lazy { StatsGrpc.newStub(channel) }

    fun pullEverything(empty: Empty) = makeGrpcCall(channel, curry(asyncStub::syncDown)(empty)) {
        onError {
            BasicNetworkState.Error(it.localizedMessage ?: "Unknown error")
        }
    }

    fun pushEverything(formSync: FormSyncMessage) = makeGrpcCall(channel, curry(asyncStub::syncUp)(formSync)) {
        onError {
            BasicNetworkState.Error(it.localizedMessage ?: "Unknown error")
        }
    }
}