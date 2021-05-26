package com.safidence.safidence.ui.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class PasswordFragment : Fragment() {

    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        passwordViewModel =
                ViewModelProvider(this).get(PasswordViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_password, container, false)

        return root
    }
}