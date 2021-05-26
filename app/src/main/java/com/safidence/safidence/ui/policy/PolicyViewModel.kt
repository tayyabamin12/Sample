package com.safidence.safidence.ui.policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PolicyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is policy Fragment"
    }
    val text: LiveData<String> = _text
}