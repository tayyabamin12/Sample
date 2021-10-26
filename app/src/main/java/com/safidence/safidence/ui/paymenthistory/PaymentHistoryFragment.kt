package com.safidence.safidence.ui.paymenthistory

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
import com.safidence.safidence.adapters.PaymentHistoryAdapter
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.BodyPaymentHistory
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentPaymentHistoryBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class PaymentHistoryFragment : Fragment() {

    private lateinit var paymentHistoryViewModel: PaymentHistoryViewModel
    private var _binding: FragmentPaymentHistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        setRecyclerView(root)
        setupObserver()

        paymentHistoryViewModel.getPaymentHistory(SavePref(requireContext()).getAccessToken())
        showProgressDialog()

        return root
    }

    private fun setupViewModel() {
        paymentHistoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(PaymentHistoryViewModel::class.java)
    }

    private fun setupObserver() {
        paymentHistoryViewModel.getResponsePaymentHistory().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                if (it.body != null) {
                    setRecyclerView(it.body)
                }else if (it.message != null) {
                    binding.tvMessage.text = it.message
                }
            }
        })

        paymentHistoryViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setRecyclerView(list: List<BodyPaymentHistory>) {
        binding.rvHistory.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PaymentHistoryAdapter(list)
        }
    }

    private lateinit var progressDialog: ProgressDialog
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage(resources.getString(R.string.please_wait))
        progressDialog.show()
    }

    private fun dismissDialog() {
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}