import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.R
import com.example.apptakeaway.model.Order
import java.text.SimpleDateFormat
import java.util.Locale

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        val orderDateTextView: TextView = itemView.findViewById(R.id.orderDateTextView)
        val orderTotalTextView: TextView = itemView.findViewById(R.id.orderTotalTextView)
        val orderStatusTextView: TextView = itemView.findViewById(R.id.orderStatusTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderIdTextView.text = "Orden ID: ${order.id}"
        holder.orderDateTextView.text = "Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(order.date)}"
        holder.orderTotalTextView.text = "Total: ${order.total}"
        holder.orderStatusTextView.text = "Estado: ${order.status}"
    }

    override fun getItemCount(): Int = orders.size
}