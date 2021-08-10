package com.safidence.safidence.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.repository.MainRepository
import com.safidence.safidence.ui.login.LoginViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(MainRepository(apiHelper)) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}