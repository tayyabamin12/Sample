package com.safidence.safidence.ui.docs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.adapters.DocsAdapter

class DocsFragment : Fragment() {

    private lateinit var docsViewModel: DocsViewModel
    private lateinit var cvNewDoc: CardView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        docsViewModel =
                ViewModelProvider(this).get(DocsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_docs, container, false)
        initViews(root)
        setRecyclerView(root)

        return root
    }

    private fun initViews(root: View) {
        cvNewDoc = root.findViewById(R.id.cv_new_doc)
        cvNewDoc.setOnClickListener {
            findNavController().navigate(R.id.action_nav_docs_to_nav_doc_new)
        }
    }

    private fun setRecyclerView(root: View) {
        val requestRV = root.findViewById<RecyclerView>(R.id.rv_requests)
        requestRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = DocsAdapter()
        }
    }
}