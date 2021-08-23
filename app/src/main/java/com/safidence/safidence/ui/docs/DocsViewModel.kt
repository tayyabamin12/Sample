package com.safidence.safidence.ui.docs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.model.ResponseAllDocuments
import com.safidence.safidence.data.repository.MainRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DocsViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val responseAllDocs = MutableLiveData<ResponseAllDocuments>()
    private val exceptionResponse = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    fun getAllDocs(token:String) {
        compositeDisposable.add(
            mainRepository.getAllDocs(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ items ->
                    responseAllDocs.postValue(items)
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

    fun getResponseAllDocs(): LiveData<ResponseAllDocuments> {
        return responseAllDocs
    }

    fun getExceptionResponse(): LiveData<String> {
        return exceptionResponse
    }
}