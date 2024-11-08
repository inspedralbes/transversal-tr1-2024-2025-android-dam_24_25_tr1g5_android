package com.example.apptakeaway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.R
import com.example.apptakeaway.adapter.OrderLineAdapter
import com.example.apptakeaway.model.Order

class OrderAdapter(private var orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    // ViewHolder...
    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderTextView: TextView = view.findViewById(R.id.orderTextView)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
        val toggleButton: Button = view.findViewById(R.id.toggleOrderLinesButton)
        val orderLinesRecyclerView: RecyclerView = view.findViewById(R.id.orderLinesRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderTextView.text = "Orden #${order.id} - Total: $${order.total}"
        holder.orderStatus.text = "Estado: ${order.status}"

        // Configuración de OrderLines...
        val orderLineAdapter = OrderLineAdapter(order.orderLines)
        holder.orderLinesRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.orderLinesRecyclerView.adapter = orderLineAdapter

        // Toggle de detalles
        holder.toggleButton.setOnClickListener {
            if (holder.orderLinesRecyclerView.visibility == View.GONE) {
                holder.orderLinesRecyclerView.visibility = View.VISIBLE
                holder.toggleButton.text = "Ocultar Detalles"
            } else {
                holder.orderLinesRecyclerView.visibility = View.GONE
                holder.toggleButton.text = "Ver Detalles"
            }
        }
    }

    override fun getItemCount(): Int = orders.size

    // Método para actualizar solo el status de una orden específica
    fun updateOrderStatus(orderId: Int, newStatus: String) {
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index].status = newStatus
            notifyItemChanged(index, "status")
        }
    }
}
