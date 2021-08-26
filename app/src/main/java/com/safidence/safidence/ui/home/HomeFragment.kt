package com.safidence.safidence.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.safidence.safidence.R
import com.safidence.safidence.data.prefs.SavePref
import com.safidence.safidence.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initViews()
        return root
    }

    private fun initViews() {
        binding.tvTitle.text = "Hello ".plus(SavePref(requireContext()).getUserName()).plus("!")

        binding.cvRequest.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_request)
        }
        binding.cvPayments.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_payment)
        }
        binding.cvAnnouncements.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_announcements)
        }
        binding.cvDocuments.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_docs)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}