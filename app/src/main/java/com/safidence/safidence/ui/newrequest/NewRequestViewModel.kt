package com.safidence.safidence.ui.newrequest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.model.ResponseRequestTypes
import com.safidence.safidence.data.model.ResponseTenantUnits
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class NewRequestViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responseRequestTypes = MutableLiveData<ResponseRequestTypes>()
    private val responseTenantUnits = MutableLiveData<ResponseTenantUnits>()
    private val uploadTenantRequest = MutableLiveData<ResponseGeneralMessage>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getRequestTypes(token:String) {
        compositeDisposable.add(
            mainRepository.getRequestTypes(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseRequestTypes.postValue(items)
                }, { throwable ->
                    Log.d("content view model", "exception")
                    exceptionResponse.postValue(throwable.localizedMessage)
                })
        )
    }

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

    fun tenantRequest(token: String,
                      requestType: Int,
                      subject: String,
                      desc: String,
                      priority: String,
                      availability: String,
                      phone: String,
                      unitId: Int, media: File) {
        compositeDisposable.add(
            mainRepository.tenantRequest(token, requestType, subject, desc, priority, availability,
                phone, unitId, media)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    uploadTenantRequest.postValue(items)
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

    fun getResponseReqTypes(): LiveData<ResponseRequestTypes> {
        return responseRequestTypes
    }

    fun getResponseTenantUnits(): LiveData<ResponseTenantUnits> {
        return responseTenantUnits
    }

    fun getTenantRequestResponse(): LiveData<ResponseGeneralMessage> {
        return uploadTenantRequest
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}