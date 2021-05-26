package com.safidence.safidence.ui.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.MainActivity
import com.safidence.safidence.R
import com.safidence.safidence.adapters.RequestAdapter

class RequestFragment : Fragment() {

    private lateinit var requestViewModel: RequestViewModel
    private lateinit var cvNewRequest: CardView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requestViewModel =
                ViewModelProvider(this).get(RequestViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_requests, container, false)
        initViews(root)
        setRecyclerView(root)

        return root
    }

    private fun initViews(root: View) {
        cvNewRequest = root.findViewById(R.id.cv_new_request)
        cvNewRequest.setOnClickListener {
            findNavController().navigate(R.id.action_nav_request_to_nav_new_request)
        }
    }

    private fun setRecyclerView(root: View) {
        val requestRV = root.findViewById<RecyclerView>(R.id.rv_requests)
        requestRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RequestAdapter()
        }
    }
}