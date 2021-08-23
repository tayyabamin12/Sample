package com.safidence.safidence.ui.newdoc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseDocTypes
import com.safidence.safidence.data.model.ResponseGeneralMessage
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class NewDocViewModel(private val mainRepository: MainRepository)  : ViewModel() {

    private val responseDocTypes = MutableLiveData<ResponseDocTypes>()
    private val responseUploadDoc = MutableLiveData<ResponseGeneralMessage>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getDocTypes(token:String) {
        compositeDisposable.add(
            mainRepository.getDocTypes(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseDocTypes.postValue(items)
                }, { throwable ->
                    Log.d("content view model", "exception")
                    exceptionResponse.postValue(throwable.localizedMessage)
                })
        )
    }

    fun uploadDoc(token: String, docId: Int, num: String, country: String,
                      date: String, media: File) {
        compositeDisposable.add(
            mainRepository.uploadDoc(token, docId, num, country, date, media)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseUploadDoc.postValue(items)
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

    fun getResponseDocTypes(): LiveData<ResponseDocTypes> {
        return responseDocTypes
    }

    fun getResponseUploadDoc(): LiveData<ResponseGeneralMessage> {
        return responseUploadDoc
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}