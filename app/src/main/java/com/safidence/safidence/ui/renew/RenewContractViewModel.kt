package com.safidence.safidence.ui.renew

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.model.ResponseTenantContractExpiry
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RenewContractViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responseTenantUnits = MutableLiveData<ResponseTenantUnits>()
    private val responseTenantContractExpiry = MutableLiveData<ResponseTenantContractExpiry>()
    private val responseContractRequest = MutableLiveData<ResponseGeneralMessage>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getTenantUnits(token:String) {
        compositeDisposable.add(
            mainRepository.getTenantUnits(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseTenantUnits.postValue(items)
                }, { throwable ->
                    Log.d("content view model", "exception")
                    exceptionResponse.postValue(throwable.localizedMessage)
                })
        )
    }

    fun getTenantContractExpiry(token:String, userId:Int) {
        compositeDisposable.add(
            mainRepository.getTenantContractExpiry(token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseTenantContractExpiry.postValue(items)
                }, { throwable ->
                    Log.d("content view model", "exception")
                    exceptionResponse.postValue(throwable.localizedMessage)
                })
        )
    }

    fun contractRequest(token: String, expiryDate: String, date: String, unitId: Int,
                        isRenew: Boolean) {
        compositeDisposable.add(
            mainRepository.contractRequest(token, expiryDate, date, unitId, isRenew)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseContractRequest.postValue(items)
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

    fun getResponseTenantUnits(): LiveData<ResponseTenantUnits> {
        return responseTenantUnits
    }

    fun getResponseTenantContractExpiry(): LiveData<ResponseTenantContractExpiry> {
        return responseTenantContractExpiry
    }

    fun getResponseContractRequest(): LiveData<ResponseGeneralMessage> {
        return responseContractRequest
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}