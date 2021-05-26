package com.safidence.safidence.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class LoginFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_login, container, false)
        /*val textView: TextView = root.findViewById(R.id.tv_title)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })*/
        return root
    }
}