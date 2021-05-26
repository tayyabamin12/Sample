package com.safidence.safidence.ui.newrequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewRequestViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is request Fragment"
    }
    val text: LiveData<String> = _text
}