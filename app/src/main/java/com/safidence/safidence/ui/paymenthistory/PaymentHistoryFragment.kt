package com.safidence.safidence.ui.paymenthistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.adapters.PaymentHistoryAdapter
import com.safidence.safidence.adapters.RequestAdapter

class PaymentHistoryFragment : Fragment() {

    private lateinit var paymentHistoryViewModel: PaymentHistoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        paymentHistoryViewModel =
                ViewModelProvider(this).get(PaymentHistoryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_payment_history, container, false)
        setRecyclerView(root)

        return root
    }

    private fun setRecyclerView(root: View) {
        val requestRV = root.findViewById<RecyclerView>(R.id.rv_history)
        requestRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PaymentHistoryAdapter()
        }
    }
}