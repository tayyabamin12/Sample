package com.safidence.safidence.ui.policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.safidence.safidence.data.repository.MainRepository

class PolicyViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is policy Fragment"
    }
    val text: LiveData<String> = _text
}