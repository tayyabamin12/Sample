package com.safidence.safidence.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.data.model.PoliciesBody
import com.safidence.safidence.utils.Util

class PolicyAdapter(private val context: Context, private val list: List<PoliciesBody>) : RecyclerView.Adapter<PolicyAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_title)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_policy, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position].title

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", Util.getPdfBaseUrl().plus(list.get(position).file))
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_nav_policy_to_nav_pdf, bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}