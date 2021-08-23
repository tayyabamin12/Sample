package com.safidence.safidence.ui.announcement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseAlerts
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AlertsViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responseAlerts = MutableLiveData<ResponseAlerts>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getAlerts(token:String) {
        compositeDisposable.add(
            mainRepository.getTenantAlerts(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseAlerts.postValue(items)
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

    fun getResponseAlerts(): LiveData<ResponseAlerts> {
        return responseAlerts
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}