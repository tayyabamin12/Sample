package com.safidence.safidence.ui.property

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R

class PropertyFragment : Fragment() {

    private lateinit var propertyViewModel: PropertyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        propertyViewModel =
                ViewModelProvider(this).get(PropertyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_property, container, false)

        return root
    }
}