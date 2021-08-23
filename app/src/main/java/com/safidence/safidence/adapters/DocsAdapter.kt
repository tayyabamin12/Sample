package com.safidence.safidence.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.data.model.DocBody
import com.safidence.safidence.data.model.RequestStatusBody
import java.text.SimpleDateFormat
import java.util.*

class DocsAdapter(private val list: List<DocBody>) : RecyclerView.Adapter<DocsAdapter.ViewHolder>()  {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_docs, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = "Document No: ".plus(list[position].number)

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val output = formatter.format(parser.parse(list[position].expiry_date))

        holder.tvDate.text = "Expired On: ".plus(output)
        holder.tvStatus.text = "Issued By: ".plus(list[position].issue_country)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}