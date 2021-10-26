package com.safidence.safidence.ui.docs

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
import com.safidence.safidence.adapters.DocsAdapter
import com.safidence.safidence.api.ApiHelper
import com.safidence.safidence.api.ApiServiceImpl
import com.safidence.safidence.data.model.DocBody
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentDocsBinding
import com.safidence.safidence.ui.base.ViewModelFactory

class DocsFragment : Fragment() {

    private lateinit var docsViewModel: DocsViewModel
    private var _binding: FragmentDocsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        _binding = FragmentDocsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews()
        setupObserver()

        docsViewModel.getAllDocs(SavePref(requireContext()).getAccessToken())
        showProgressDialog()

        return root
    }

    private fun setupViewModel() {
        docsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(ApiServiceImpl()))
        ).get(DocsViewModel::class.java)
    }

    private fun initViews() {
        binding.cvNewDoc.setOnClickListener {
            findNavController().navigate(R.id.action_nav_docs_to_nav_doc_new)
        }
    }

    private fun setRecyclerView(list: List<DocBody>) {
        binding.rvRequests.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = DocsAdapter(list)
        }
    }

    private fun setupObserver() {
        docsViewModel.getResponseAllDocs().observe(viewLifecycleOwner, Observer {
            dismissDialog()
            if (it.status == "success") {
                setRecyclerView(it.body)
            }
        })

        docsViewModel.getExceptionResponse().observe(viewLifecycleOwner, Observer {
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