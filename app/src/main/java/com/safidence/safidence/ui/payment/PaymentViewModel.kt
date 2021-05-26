package com.safidence.safidence.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is payment Fragment"
    }
    val text: LiveData<String> = _text
}