package com.safidence.safidence.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.data.model.AlertsBody

class RequestAdapter(private val list: List<AlertsBody>) : RecyclerView.Adapter<RequestAdapter.ViewHolder>()  {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_request,
            parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = list[position].message

        holder.tvDate.text = "Event Time: ".plus(list[position].event_time)
        holder.tvStatus.text = "Instructions: ".plus(list[position].instructions)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}