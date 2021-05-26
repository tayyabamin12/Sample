package com.safidence.safidence.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R

class PaymentFragment : Fragment() {

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var cvCreditCard: CardView
    private lateinit var cvCashPayment: CardView
    private lateinit var cvBankDeposit: CardView
    private lateinit var cvCheque: CardView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        paymentViewModel =
                ViewModelProvider(this).get(PaymentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_payment, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        cvCreditCard = root.findViewById(R.id.cv_credit_card)
        cvCashPayment = root.findViewById(R.id.cv_cash_payment)
        cvBankDeposit = root.findViewById(R.id.cv_bank_deposit)
        cvCheque = root.findViewById(R.id.cv_cheque)

        cvCreditCard.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_credit_card)
        }
        cvCashPayment.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_cash_payment)
        }
        cvBankDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_bank_deposit)
        }
        cvCheque.setOnClickListener {
            findNavController().navigate(R.id.action_nav_payment_to_nav_cheque)
        }
    }
}