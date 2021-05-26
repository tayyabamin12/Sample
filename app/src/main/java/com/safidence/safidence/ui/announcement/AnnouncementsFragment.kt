package com.safidence.safidence.ui.announcement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.adapters.RequestAdapter

class AnnouncementsFragment : Fragment() {

    private lateinit var announcementsViewModel: AnnouncementsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        announcementsViewModel =
                ViewModelProvider(this).get(AnnouncementsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_announcements, container, false)
        setRecyclerView(root)

        return root
    }

    private fun setRecyclerView(root: View) {
        val requestRV = root.findViewById<RecyclerView>(R.id.rv_announcements)
        requestRV.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = RequestAdapter()
        }
    }
}