package com.safidence.safidence.ui.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class EndContractFragment : Fragment() {

    private lateinit var endContractViewModel: EndContractViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        endContractViewModel =
                ViewModelProvider(this).get(EndContractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_end_contract, container, false)

        return root
    }
}