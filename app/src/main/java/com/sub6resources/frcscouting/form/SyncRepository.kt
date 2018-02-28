package com.sub6resources.frcscouting.form

import com.google.protobuf.Empty
import com.sub6resources.frcscouting.login.BasicNetworkState
import com.sub6resources.frcscouting.login.curry
import com.sub6resources.frcscouting.login.makeGrpcCall
import io.grpc.CallCredentials
import io.grpc.ManagedChannel
import io.grpc.stub.MetadataUtils
import io.grpc.Metadata
import stats.*

typealias FormSyncMessage = FormSyncOuterClass.FormSync
typealias FormMessage = FormOuterClass.Form
typealias FieldMessage = FieldOuterClass.Field
typealias ChoiceMessage = ChoiceOuterClass.Choice
typealias FormResponseMessage = FormResponseOuterClass.FormResponse
typealias FieldResponseMessage = FieldResponseOuterClass.FieldResponse

class SyncRepository(private val channel: ManagedChannel) {
    //val header by lazy {Metadata.Key<String>()}
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