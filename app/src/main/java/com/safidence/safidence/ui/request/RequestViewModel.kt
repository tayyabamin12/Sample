package com.safidence.safidence.ui.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.model.ResponseRequestStatus
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RequestViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val response = MutableLiveData<ResponseRequestStatus>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getTenantRequestStatus(token:String) {
        compositeDisposable.add(
            mainRepository.getTenantRequestStatus(token)
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

    fun getResponse(): LiveData<ResponseRequestStatus> {
        return response
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}