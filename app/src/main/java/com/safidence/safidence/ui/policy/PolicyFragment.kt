package com.safidence.safidence.ui.policy

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.safidence.safidence.R
import com.safidence.safidence.adapters.PolicyAdapter
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.PoliciesBody
import com.safidence.safidence.data.prefs.SavePref
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
        setupObserver()

        policyViewModel.getPolicies(SavePref(requireContext()).getAccessToken())
        showProgressDialog()
        return root
    }
    private fun setupViewModel() {
        policyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PolicyViewModel::class.java)
    }

    private fun setRecyclerView(listBody: List<PoliciesBody>) {
        if (listBody.isEmpty())
            binding.tvMessage.visibility = View.VISIBLE
        binding.rvAnnouncements.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PolicyAdapter(context, listBody)
        }
    }

    private fun setupObserver() {
        policyViewModel.getResponsePolicies().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                setRecyclerView(it.body)
            }
        })

        policyViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized && progressDialog != null)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}