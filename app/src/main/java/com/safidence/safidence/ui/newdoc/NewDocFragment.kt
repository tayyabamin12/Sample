package com.safidence.safidence.ui.newdoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R
import java.util.ArrayList

class NewDocFragment : Fragment() {

    private lateinit var newDocViewModel: NewDocViewModel
    private lateinit var spinnerDocType: Spinner

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newDocViewModel =
                ViewModelProvider(this).get(NewDocViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_doc, container, false)
        spinnerDocType = root.findViewById(R.id.s_type)
        setDocTypeSpinner()

        return root
    }

    private fun setDocTypeSpinner() {
        val list: ArrayList<String> = ArrayList()

        list.add("Passport")
        list.add("National ID")
        list.add("Driving License")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDocType.adapter = arrayAdapter

        spinnerDocType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}