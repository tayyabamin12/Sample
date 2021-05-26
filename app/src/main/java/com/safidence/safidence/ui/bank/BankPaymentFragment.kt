package com.safidence.safidence.ui.bank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class BankPaymentFragment : Fragment() {

    private lateinit var bankPaymentViewModel: BankPaymentViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        bankPaymentViewModel =
                ViewModelProvider(this).get(BankPaymentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bank_deposit, container, false)

        return root
    }
}