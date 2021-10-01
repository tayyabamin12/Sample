package com.safidence.safidence.ui.policy

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponsePolicies
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PolicyViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responsePolicies = MutableLiveData<ResponsePolicies>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getPolicies(token:String) {
        compositeDisposable.add(
            mainRepository.getPolicies(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responsePolicies.postValue(items)
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

    fun getResponsePolicies(): LiveData<ResponsePolicies> {
        return responsePolicies
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}