package com.safidence.safidence.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.safidence.safidence.R
import com.safidence.safidence.data.model.BodyPaymentHistory

class PaymentHistoryAdapter(private val list: List<BodyPaymentHistory>) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvMethod: TextView = itemView.findViewById(R.id.tv_method)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_payment_history, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = "Payment: QAR ".plus(list[position].Payment)
        holder.tvDate.text = "Paid On: ".plus(list[position].paid_on)
        holder.tvMethod.text = "Payment Method: ".plus(list[position].payment_method)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}