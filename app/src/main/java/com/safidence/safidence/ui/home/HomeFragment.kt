package com.safidence.safidence.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.prefs.SavePref

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var cvRequest: CardView
    private lateinit var cvPayment: CardView
    private lateinit var cvAnnoucements: CardView
    private lateinit var cvDocs: CardView
    private lateinit var tvName: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        tvName = root.findViewById(R.id.tv_title)
        tvName.text = "Hello ".plus(SavePref(requireContext()).getUserName()).plus("!")
        cvRequest = root.findViewById(R.id.cv_request)
        cvPayment = root.findViewById(R.id.cv_Payments)
        cvAnnoucements = root.findViewById(R.id.cv_announcements)
        cvDocs = root.findViewById(R.id.cv_documents)

        cvRequest.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_request)
        }
        cvPayment.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_payment)
        }
        cvAnnoucements.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_announcements)
        }
        cvDocs.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_docs)
        }
    }
}