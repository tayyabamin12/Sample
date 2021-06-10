package com.safidence.safidence.ui.newdoc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.safidence.safidence.R
import java.util.ArrayList

class NewDocFragment : Fragment() {

    private lateinit var newDocViewModel: NewDocViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newDocViewModel =
                ViewModelProvider(this).get(NewDocViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_doc, container, false)

        setDocTypeSpinner(root)

        return root
    }

    private fun setDocTypeSpinner(root: View) {
        val spinner = root.findViewById<AutoCompleteTextView>(R.id.ac_doc_type)
        val selections = arrayOf("Passport", "National ID", "Driving License")

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, selections)
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}