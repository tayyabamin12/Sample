package com.safidence.safidence.ui.contract

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseContract
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContractViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responseTenantUnits = MutableLiveData<ResponseTenantUnits>()
    private val responseContract = MutableLiveData<ResponseContract>()
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

    fun getContractDetails(token:String, unitId: Int) {
        compositeDisposable.add(
            mainRepository.getContractDetails(token, unitId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseContract.postValue(items)
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

    fun getResponsePolicies(): LiveData<ResponseContract> {
        return responseContract
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}