package com.safidence.safidence.ui.creditcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreditCardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is password Fragment"
    }
    val text: LiveData<String> = _text
}