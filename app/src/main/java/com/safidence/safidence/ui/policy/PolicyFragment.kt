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

class PolicyFragment : Fragment() {

    private lateinit var policyViewModel: PolicyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        policyViewModel =
                ViewModelProvider(this).get(PolicyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_policy, container, false)
        setRecyclerView(root)

        return root
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
            adapter = PolicyAdapter(list)
        }
    }
}