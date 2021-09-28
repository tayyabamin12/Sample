package com.safidence.safidence.ui.pdfview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponsePaymentHistory
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PdfViewViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responsePaymentHistory = MutableLiveData<ResponsePaymentHistory>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getPaymentHistory(token:String) {
        compositeDisposable.add(
            mainRepository.getPaymentHistory(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responsePaymentHistory.postValue(items)
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

    fun getResponsePaymentHistory(): LiveData<ResponsePaymentHistory> {
        return responsePaymentHistory
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}