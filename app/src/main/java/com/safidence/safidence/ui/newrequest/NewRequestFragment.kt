package com.safidence.safidence.ui.newrequest

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

class NewRequestFragment : Fragment() {

    private lateinit var newRequestViewModel: NewRequestViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newRequestViewModel =
                ViewModelProvider(this).get(NewRequestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_request, container, false)
        setCatSpinner(root)
        setPrioritySpinner(root)

        return root
    }

    private fun setCatSpinner(root: View) {
        val spinner = root.findViewById<AutoCompleteTextView>(R.id.ac_tv_cat)
        val selections = arrayOf("General", "Electrical", "Appliance",
            "Furniture", "Locks & Keys", "Plumbing")

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item, selections)
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setPrioritySpinner(root: View) {
        val spinner = root.findViewById<AutoCompleteTextView>(R.id.ac_tv_priority)
        val selections = arrayOf("Routine", "Urgent")

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