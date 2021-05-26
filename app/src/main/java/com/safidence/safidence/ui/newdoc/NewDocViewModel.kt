package com.safidence.safidence.ui.newdoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewDocViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is request Fragment"
    }
    val text: LiveData<String> = _text
}