package com.safidence.safidence.ui.request

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.safidence.safidence.R
import com.safidence.safidence.adapters.RequestStatusAdapter
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.RequestStatusBody
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentRequestsBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class RequestFragment : Fragment() {

    private lateinit var requestViewModel: RequestViewModel
    private var _binding: FragmentRequestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews(root)
        setupObserver()

        requestViewModel.getTenantRequestStatus(SavePref(requireContext()).getAccessToken())
        showProgressDialog()

        return root
    }

    private fun setupViewModel() {
        requestViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(RequestViewModel::class.java)
    }

    private fun initViews(root: View) {
        binding.cvNewRequest.setOnClickListener {
            findNavController().navigate(R.id.action_nav_request_to_nav_new_request)
        }
    }

    private fun setupObserver() {
        requestViewModel.getResponse().observe(viewLifecycleOwner, Observer{
            dismissDialog()
            if (it.status == "success") {
                setRecyclerView(it.body)
            }
        })

        requestViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setRecyclerView(body: List<RequestStatusBody>) {

        binding.rvRequests.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RequestStatusAdapter(body)
        }
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog(){
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (progressDialog != null)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}