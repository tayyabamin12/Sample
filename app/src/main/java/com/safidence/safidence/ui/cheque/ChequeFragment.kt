package com.safidence.safidence.ui.cheque

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class ChequeFragment : Fragment() {

    private lateinit var chequeViewModel: ChequeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        chequeViewModel =
                ViewModelProvider(this).get(ChequeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_cheque, container, false)

        return root
    }
}