package com.safidence.safidence.ui.policy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.adapters.PolicyAdapter
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.databinding.FragmentPolicyBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class PolicyFragment : Fragment() {

    private lateinit var policyViewModel: PolicyViewModel
    private var _binding: FragmentPolicyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentPolicyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setRecyclerView(root)

        return root
    }
    private fun setupViewModel() {
        policyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PolicyViewModel::class.java)
    }

    private fun setRecyclerView(root: View) {
        val requestRV = root.findViewById<RecyclerView>(R.id.rv_announcements)

        val list = ArrayList<String>()
        list.add("Noise Policy")
        list.add("End Contract Policy")
        list.add("Event Management Policy")

        requestRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PolicyAdapter(context, list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}