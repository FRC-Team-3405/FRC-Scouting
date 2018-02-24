package com.sub6resources.frcscouting.login

import accounts.TokenOuterClass
import accounts.UserOuterClass
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.google.protobuf.AbstractMessage
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.MessageOrBuilder
import io.grpc.ManagedChannel
import io.grpc.stub.StreamObserver
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import stats.FormOuterClass

sealed class BasicNetworkState<T> {
    class Loading<T> : BasicNetworkState<T>()
    class Success<T>(val data: T) : BasicNetworkState<T>()
    class Error<T>(val message: String) : BasicNetworkState<T>()
}

fun <T, U> passThrough(first: T, second: U): MutableLiveData<U> = MutableLiveData<U>().apply { value = second }

infix fun <T, U> LiveData<BasicNetworkState<T>>.chainOnSuccess(next: (param: T) -> LiveData<BasicNetworkState<U>>): LiveData<BasicNetworkState<U>> {
    return Transformations.switchMap(this) {
        when (it) {
            is BasicNetworkState.Loading<T> -> passThrough(it, BasicNetworkState.Loading())
            is BasicNetworkState.Error<T> -> passThrough(it, BasicNetworkState.Error(it.message))
            is BasicNetworkState.Success<T> -> next(it.data)
        }
    }
}

class ExtrasHandler<T> {
    // force the implementation of methods. Don't worry, if you don't need an insert,
    // it won't be called

    var insertFunction: ((d: T) -> Unit)? = { Log.e("NETWORKEXTRAS", "You haven't implemented an insert function!!!") }
        private set

    var errorFunction: ((t: Throwable) -> BasicNetworkState.Error<T>) = { BasicNetworkState.Error("Unknown Error") }
        private set

    fun onError(error: (t: Throwable) -> BasicNetworkState.Error<T>) {
        errorFunction = error
    }

    fun insert(insert: (d: T) -> Unit) {
        insertFunction = insert
    }
}

inline fun <T> makeNetworkRequest(call: Single<T>, query: LiveData<T>? = null, handler: ExtrasHandler<T>.() -> Unit = {}): LiveData<BasicNetworkState<T>> {
    val mediator: MediatorLiveData<BasicNetworkState<T>> = MediatorLiveData()
    mediator.value = BasicNetworkState.Loading()
    val extrasHandler = ExtrasHandler<T>().apply { handler() }

    call.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (query == null) {
            mediator.postValue(BasicNetworkState.Success(it))
        } else {
            extrasHandler.insertFunction?.invoke(it)
            mediator.addSource(query) { mediator.postValue(BasicNetworkState.Success(it!!)) }
        }
    }, {
        mediator.postValue(extrasHandler.errorFunction.invoke(it))
    })

    return mediator
}

fun <A,B,C> curry(function: (A,B) -> C): (A) -> (B) -> C {
    return fun (a: A): (B) -> C {
        return fun (b: B): C {
            return function(a, b)
        }
    }
}

class Observer<T> {
    private lateinit var onNext: (value: T) -> Unit
    private lateinit var onError: (t: Throwable?) -> Unit
    private lateinit var onCompleted: () -> Unit

    fun next(_onNext: (value: T) -> Unit) {
        onNext = _onNext
    }

    fun error(_onError: (t: Throwable?) -> Unit) {
        onError = _onError
    }

    fun completed(_onCompleted: () -> Unit) {
        onCompleted = _onCompleted
    }

    fun build(): StreamObserver<T> {
        return object: StreamObserver<T> {
            override fun onNext(value: T) { this@Observer.onNext(value) }
            override fun onError(t: Throwable?) { this@Observer.onError(t) }
            override fun onCompleted() { this@Observer.onCompleted() }
        }
    }
}

fun <T> streamObserver(builder: Observer<T>.() -> Unit): StreamObserver<T> {
    return Observer<T>()
            .apply(builder)
            .build()
}


fun <U: GeneratedMessageV3> makeGrpcCall(channel: ManagedChannel, call: (observer: StreamObserver<U>) -> Unit, query: LiveData<U>? = null, handler: ExtrasHandler<U>.() -> Unit = {}): LiveData<BasicNetworkState<U>> {
    val mediatorLiveData = MediatorLiveData<BasicNetworkState<U>>()
    val extrasHandler = ExtrasHandler<U>().apply { handler() }
    call(streamObserver {
        next {
            if(query == null) {
                mediatorLiveData.postValue(BasicNetworkState.Success(it))
            } else {
                extrasHandler.insertFunction?.invoke(it)
                mediatorLiveData.addSource(query) { mediatorLiveData.postValue(BasicNetworkState.Success(it!!)) }
            }
        }
        error {
            mediatorLiveData.postValue(extrasHandler.errorFunction.invoke(it!!))
        }
        completed {
            // add a new thing
        }
    })
    return mediatorLiveData
}

fun check(call: (value: UserOuterClass.User, observer: StreamObserver<TokenOuterClass.Token>) -> Unit) {

}

interface builderer {
    fun newBuilder()
}

inline fun <reified T: GeneratedMessageV3> build(x: T) {
    x.newBuilderForType() as T
}

