package com.safidence.safidence.ui.property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PropertyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is property Fragment"
    }
    val text: LiveData<String> = _text
}