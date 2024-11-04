package com.example.apptakeaway

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        // Recupera los datos del Intent
        val id = intent.getIntExtra("id", -1)
        val total = intent.getDoubleExtra("total", 0.0)
        val userId = intent.getIntExtra("userId", -1)
        val date = intent.getSerializableExtra("date") as? Date
        val status = intent.getStringExtra("status") ?: "N/A"
        val pay = intent.getBooleanExtra("pay", false)

        // Formatear la fecha si está disponible
        val dateFormatted = date?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it) } ?: "N/A"

        // Referencias a las vistas de texto en activity_order.xml
        findViewById<TextView>(R.id.idTextView).text = "ID: $id"
        findViewById<TextView>(R.id.totalTextView).text = "Total: $total"
        findViewById<TextView>(R.id.userIdTextView).text = "User ID: $userId"
        findViewById<TextView>(R.id.dateTextView).text = "Fecha: $dateFormatted"
        findViewById<TextView>(R.id.statusTextView).text = "Estado: $status"
        findViewById<TextView>(R.id.payTextView).text = "Pagado: ${if (pay) "Sí" else "No"}"
    }
}
