package com.safidence.safidence.ui.newrequest

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

class NewRequestFragment : Fragment() {

    private lateinit var newRequestViewModel: NewRequestViewModel
    private lateinit var spinnerPriority: Spinner
    private lateinit var spinnerCat: Spinner

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newRequestViewModel =
                ViewModelProvider(this).get(NewRequestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_request, container, false)
        spinnerCat = root.findViewById(R.id.s_cat)
        spinnerPriority = root.findViewById(R.id.s_priority)
        setCatSpinner()
        setPrioritySpinner()

        return root
    }

    private fun setPrioritySpinner() {
        val list: ArrayList<String> = ArrayList()

        list.add("Routine")
        list.add("Urgent")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = arrayAdapter

        spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setCatSpinner() {
        val list: ArrayList<String> = ArrayList()

        list.add("General")
        list.add("Electrical")
        list.add("Appliance")
        list.add("Furniture")
        list.add("Locks & Keys")
        list.add("Plumbing")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCat.adapter = arrayAdapter

        spinnerCat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}