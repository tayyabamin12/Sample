package com.safidence.safidence.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseLogin
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val mainRepository: MainRepository)  : ViewModel() {

    private val response = MutableLiveData<ResponseLogin>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun login(nic:String, password:String) {
        compositeDisposable.add(
            mainRepository.login(nic, password)
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

    fun getLoginResponse(): LiveData<ResponseLogin> {
        return response
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}