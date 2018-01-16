package com.sub6resources.frcscouting.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Transformations
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

typealias loading<U> = (() -> U)
typealias success<T, U> = ((param: T) -> U)
typealias failure<U> = ((error: Throwable) -> U)
typealias insert<T> = ((param: T) -> Unit)

class NetworkLiveDataBuilder<T, U> {
    private var loading: loading<U>? = null
    private var success: success<T, U>? = null
    private var failure: failure<U>? = null

    fun onLoad(l: loading<U>) { loading = l }
    fun onSuccess(s: success<T, U>) { success = s }
    fun onFailure(f: failure<U>) { failure = f }

    fun build(networkCall: Single<T>): LiveData<U> {
        val mediatorLiveData: MediatorLiveData<U> = MediatorLiveData()
        mediatorLiveData.postValue(loading?.invoke())
        networkCall.subscribe({
            mediatorLiveData.postValue(success?.invoke(it))
        }, {
            mediatorLiveData.postValue(failure?.invoke(it))
        })
        return mediatorLiveData
    }
}

class RoomNetworkBuilder<T, U> {
    private var loading: loading<U>? = null
    private var success: success<T, U>? = null
    private var failure: failure<U>? = null
    private var insert: insert<T>? = null

    fun insert(i: insert<T>) { insert = i}
    fun onLoad(l: loading<U>) { loading = l}
    fun onSuccess(s: success<T, U>) { success = s }
    fun onFailure(f: failure<U>) { failure = f }

    fun build(networkCall: Single<T>, query: LiveData<T>): LiveData<U> {
        val mediatorLiveData: MediatorLiveData<U> = MediatorLiveData()
        mediatorLiveData.postValue(loading?.invoke())
        mediatorLiveData.addSource(query) { it?.let { mediatorLiveData.postValue(success?.invoke(it)) } }
        networkCall.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            this.insert?.invoke(it)
        }, {
            mediatorLiveData.postValue(failure?.invoke(it))
        })
        return mediatorLiveData
    }
}

inline fun <T, U> makeNetworkRequest(call: Single<T>, builder: NetworkLiveDataBuilder<T, U>.() -> Unit): LiveData<U> {
    val b = NetworkLiveDataBuilder<T, U>()
    b.apply(builder)
    return b.build(call)
}

inline fun <T, U> makeNetworkRequest(call: Single<T>, query: LiveData<T>, builder: RoomNetworkBuilder<T, U>.() -> Unit): LiveData<U> {
    val b = RoomNetworkBuilder<T, U>()
    b.apply(builder)
    return b.build(call, query)
}

infix fun <T, U> LiveData<T>.chain(next: (param: T) -> LiveData<U>): LiveData<U> {
    return Transformations.switchMap(this) { next(it) }
}