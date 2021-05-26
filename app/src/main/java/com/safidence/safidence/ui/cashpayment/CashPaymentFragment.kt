package com.safidence.safidence.ui.cashpayment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class CashPaymentFragment : Fragment() {

    private lateinit var cashPaymentViewModel: CashPaymentViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cashPaymentViewModel =
                ViewModelProvider(this).get(CashPaymentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cash_payment, container, false)

        return root
    }
}