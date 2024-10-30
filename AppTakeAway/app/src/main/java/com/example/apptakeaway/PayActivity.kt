package com.example.apptakeaway // Asegúrate de que esto coincida con tu estructura de paquetes

import android.os.Bundle
import android.util.Log // Importa Log para los registros
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.apptakeaway.viewmodel.PayViewModel

class PayActivity : AppCompatActivity() {

    private lateinit var creditCardRadioButton: RadioButton
    private lateinit var payInShopRadioButton: RadioButton
    private lateinit var payViewModel: PayViewModel // Nueva referencia al PayViewModel
    private lateinit var textPriceTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay) // Establece el layout para esta actividad

        // Inicializa el ViewModel
        payViewModel = ViewModelProvider(this).get(PayViewModel::class.java)

        // Inicializa el TextView aquí después de setContentView
        textPriceTotal = findViewById(R.id.textPriceTotal) // Asegúrate de que este ID es correcto

        setupBackButton()
        setupPaymentOptions()

        // Observa los items de pago y actualiza el total
        payViewModel.payItems.observe(this) { payItems ->
            Log.d("PayActivity", "PayItems: $payItems")
            textPriceTotal = findViewById(R.id.textPriceTotal)// Muestra los elementos de pago en logcat
            updateTotal() // Llama a updateTotal al recibir cambios en payItems
        }

    }

    private fun setupBackButton() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish() // Cierra la actividad actual y vuelve a la anterior
        }
    }

    private fun setupPaymentOptions() {
        creditCardRadioButton = findViewById(R.id.creditCardRadioButton)
        payInShopRadioButton = findViewById(R.id.payInShopRadioButton)

        // Configura los contenedores de los RadioButtons
        setupPaymentOptionClick(R.id.credit_card_box, creditCardRadioButton)
        setupPaymentOptionClick(R.id.pay_in_shop_box, payInShopRadioButton)

        // Configura los RadioButtons directamente
        creditCardRadioButton.setOnClickListener { selectOption(creditCardRadioButton) }
        payInShopRadioButton.setOnClickListener { selectOption(payInShopRadioButton) }
    }

    private fun setupPaymentOptionClick(boxId: Int, radioButton: RadioButton) {
        findViewById<LinearLayout>(boxId).setOnClickListener {
            selectOption(radioButton)
        }
    }

    private fun selectOption(selectedRadioButton: RadioButton) {
        creditCardRadioButton.isChecked = selectedRadioButton == creditCardRadioButton
        payInShopRadioButton.isChecked = selectedRadioButton == payInShopRadioButton
    }

    private fun updateTotal() {
        val total = payViewModel.getPayTotal() // Asegúrate de que este método está definido en tu ViewModel
        textPriceTotal.text = String.format("", total) // Muestra el total en el TextView
    }
}
