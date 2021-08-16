package com.safidence.safidence.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository)  : ViewModel() {

    private val response = MutableLiveData<ResponseGeneralMessage>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun logout(token:String) {
        compositeDisposable.add(
            mainRepository.logout(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    response.postValue(items)
                }, { throwable ->
                    Log.d("content view model", "exception")
                    exceptionResponse.postValue(throwable.localizedMessage)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getLogoutResponse(): LiveData<ResponseGeneralMessage> {
        return response
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}