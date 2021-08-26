package com.safidence.safidence.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.repository.MainRepository
import com.safidence.safidence.ui.announcement.AlertsViewModel
import com.safidence.safidence.ui.changepassword.PasswordViewModel
import com.safidence.safidence.ui.docs.DocsViewModel
import com.safidence.safidence.ui.end.EndContractViewModel
import com.safidence.safidence.ui.forgetpassword.ForgetViewModel
import com.safidence.safidence.ui.login.LoginViewModel
import com.safidence.safidence.ui.main.MainViewModel
import com.safidence.safidence.ui.newdoc.NewDocViewModel
import com.safidence.safidence.ui.newrequest.NewRequestViewModel
import com.safidence.safidence.ui.policy.PolicyViewModel
import com.safidence.safidence.ui.profile.ProfileViewModel
import com.safidence.safidence.ui.property.PropertyViewModel
import com.safidence.safidence.ui.renew.RenewContractViewModel
import com.safidence.safidence.ui.request.RequestViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(ForgetViewModel::class.java) -> {
                ForgetViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(PasswordViewModel::class.java) -> {
                PasswordViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(NewRequestViewModel::class.java) -> {
                NewRequestViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(RequestViewModel::class.java) -> {
                RequestViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(RenewContractViewModel::class.java) -> {
                RenewContractViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(EndContractViewModel::class.java) -> {
                EndContractViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(NewDocViewModel::class.java) -> {
                NewDocViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(DocsViewModel::class.java) -> {
                DocsViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(AlertsViewModel::class.java) -> {
                AlertsViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(PropertyViewModel::class.java) -> {
                PropertyViewModel(MainRepository(apiHelper)) as T
            }
            modelClass.isAssignableFrom(PolicyViewModel::class.java) -> {
                PolicyViewModel(MainRepository(apiHelper)) as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}