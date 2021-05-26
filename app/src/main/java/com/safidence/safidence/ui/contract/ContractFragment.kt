package com.safidence.safidence.ui.contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class ContractFragment : Fragment() {

    private lateinit var contractViewModel: ContractViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        contractViewModel =
                ViewModelProvider(this).get(ContractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contract, container, false)

        return root
    }
}