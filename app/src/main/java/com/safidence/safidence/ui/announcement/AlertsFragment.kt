package com.safidence.safidence.ui.announcement

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
import com.safidence.safidence.adapters.RequestAdapter
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.AlertsBody
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentAnnouncementsBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class AlertsFragment : Fragment() {

    private lateinit var alertsViewModel: AlertsViewModel
    private var _binding: FragmentAnnouncementsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews()
        setupObserver()

        alertsViewModel.getAlerts(SavePref(requireContext()).getAccessToken())
        showProgressDialog()

        return root
    }

    private fun setupViewModel() {
        alertsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(AlertsViewModel::class.java)
    }

    private fun initViews() {
    }

    private fun setRecyclerView(listData: List<AlertsBody>) {
        binding.rvAnnouncements.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RequestAdapter(listData)
        }
    }

    private fun setupObserver() {
        alertsViewModel.getResponseAlerts().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                setRecyclerView(it.body)
            }
        })

        alertsViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
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
        if (this::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}