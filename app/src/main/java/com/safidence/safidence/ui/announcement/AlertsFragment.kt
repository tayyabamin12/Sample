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
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.adapters.RequestAdapter
import com.safidence.safidence.data.api.ApiHelper
import com.safidence.safidence.data.api.ApiServiceImpl
import com.safidence.safidence.data.model.AlertsBody
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.ui.base.ViewModelFactory

class AlertsFragment : Fragment() {

    private lateinit var alertsViewModel: AlertsViewModel
    private lateinit var rvRecyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        alertsViewModel =
                ViewModelProvider(this).get(AlertsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_announcements, container, false)
        initViews(root)
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

    private fun initViews(root: View) {
        rvRecyclerView = root.findViewById(R.id.rv_announcements)
    }

    private fun setRecyclerView(listData: List<AlertsBody>) {
        rvRecyclerView.apply {
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
        if (progressDialog != null)
            progressDialog.dismiss()
    }
}