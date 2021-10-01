package com.safidence.safidence.ui.creditcard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CreditCardViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val response = MutableLiveData<ResponseGeneralMessage>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun addCreditCardPayment(token: String, type: String, date: String, payTill:String, unitId: String,
                             amount: String, creditCardNo: String, expiryDate: String,
                             securityCode: String) {
        compositeDisposable.add(
            mainRepository.addCreditCardPayment(token, type, date, payTill, unitId, amount, creditCardNo,
                expiryDate, securityCode)
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

    fun getResponse(): LiveData<ResponseGeneralMessage> {
        return response
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}