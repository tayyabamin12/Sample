package com.safidence.safidence.ui.renew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class RenewContractFragment : Fragment() {

    private lateinit var renewContractViewModel: RenewContractViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        renewContractViewModel =
                ViewModelProvider(this).get(RenewContractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_renew_contract, container, false)

        return root
    }
}