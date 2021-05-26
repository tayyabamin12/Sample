package com.safidence.safidence.ui.creditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class CreditCardFragment : Fragment() {

    private lateinit var creditCardViewModel: CreditCardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        creditCardViewModel =
                ViewModelProvider(this).get(CreditCardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_credit_card, container, false)

        return root
    }
}