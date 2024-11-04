package com.example.apptakeaway

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptakeaway.adapter.PayAdapter
import com.example.apptakeaway.api.CreditCardRepository
import com.example.apptakeaway.api.OrderRepository
import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.CartItem
import com.example.apptakeaway.model.CreditCard
import com.example.apptakeaway.model.OrderRequest
import com.example.apptakeaway.model.ProductOrder
import com.example.apptakeaway.model.Total
import com.example.apptakeaway.viewmodel.CartViewModel

class PayActivity : AppCompatActivity() {

    private lateinit var creditCardRadioButton: RadioButton
    private lateinit var payInShopRadioButton: RadioButton
    private lateinit var textPriceTotal: TextView
    private lateinit var totalProductsText: TextView
    private lateinit var payNowButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var payAdapter: PayAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var paymentMethodGroup: RadioGroup

    private var userId: Int = -1 // Declara la variable para almacenar el userId
    private var isLoggedIn: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        // Recupera `payItems` y `userId` de los extras del Intent
        val payItems = intent.getSerializableExtra("payItems") as? ArrayList<CartItem> ?: arrayListOf()
        userId = intent.getIntExtra("userId", -1) // Asigna el valor de `userId` recibido
        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Configura el adaptador y asigna la lista de `payItems`
        payAdapter = PayAdapter(payItems)
        recyclerView.adapter = payAdapter

        // Inicializa el TextView para mostrar el total
        textPriceTotal = findViewById(R.id.textPriceTotal)
        totalProductsText = findViewById(R.id.totalProductsText)
        payNowButton = findViewById(R.id.payNowButton)
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup)

        // Actualiza el precio total
        updateTotalPrice(payItems)
        updateTotalProducts(payItems)
        setupPayButton(payItems)
        setupPaymentOptions()
        setupBackButton()
    }

    override fun onBackPressed() {
        cartViewModel.clearPayItems() // Asegúrate de que esta función exista en CartViewModel
        super.onBackPressed()
    }

    private fun updateTotalPrice(payItems: List<CartItem>) {
        val total = payItems.sumOf { it.product.price * it.quantity }
        textPriceTotal.text = String.format("$%.2f", total)
    }

    private fun updateTotalProducts(payItems: List<CartItem>) {
        val totalProducts = payItems.sumOf { it.quantity }
        totalProductsText.text = "My cart ($totalProducts)"
    }

    private fun setupBackButton() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun setupPaymentOptions() {
        creditCardRadioButton = findViewById(R.id.creditCardRadioButton)
        payInShopRadioButton = findViewById(R.id.payInShopRadioButton)

        setupPaymentOptionClick(R.id.credit_card_box, creditCardRadioButton)
        setupPaymentOptionClick(R.id.pay_in_shop_box, payInShopRadioButton)

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

    private fun setupPayButton(payItems: List<CartItem>) {
        payNowButton.setOnClickListener {
            if (payItems.isNotEmpty()) {
                processPayment(payItems)
            } else {
                Toast.makeText(this, "No hay productos seleccionados para pagar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendOrderToServer(orderRequest: OrderRequest) {
        val orderRepository = OrderRepository()

        orderRepository.placeOrder(orderRequest, onSuccess = {
            Toast.makeText(this, "Pedido realizado con éxito. ID del pedido: ${it.orderId}", Toast.LENGTH_SHORT).show()
        }, onError = { errorMessage ->
            Toast.makeText(this, "Error al realizar el pedido: $errorMessage", Toast.LENGTH_SHORT).show()
        })
    }

    private fun processPayment(payItems: List<CartItem>) {
        if (!creditCardRadioButton.isChecked && !payInShopRadioButton.isChecked) {
            Toast.makeText(this, "Por favor selecciona una opción de pago.", Toast.LENGTH_SHORT).show()
            return
        }

        val totalPrice = payItems.sumOf { it.product.price.toDouble() * it.quantity }.toString()
        val paymentMethod = if (creditCardRadioButton.isChecked) 1 else 0

        val products = payItems.map { product ->
            ProductOrder(
                id = product.product.id,
                price = product.product.price.toString(),
                quantity = product.quantity
            )
        }

        // Configura el userId solo si el usuario está autenticado
        val orderRequest = OrderRequest(
            total = Total(
                totalPrice = totalPrice,
                userId = if (isLoggedIn) userId else null, // Aquí se pasa `null` si no está autenticado
                pay = paymentMethod
            ),
            products = products
        )

        if (paymentMethod == 1) {
            initiateCreditCardPayment(payItems, orderRequest)
        } else {
            processPayInShop(payItems, orderRequest)
        }
    }



    private fun processPayInShop(payItems: List<CartItem>, orderRequest: OrderRequest) {
        // Lógica para el pago en tienda
        AlertDialog.Builder(this)
            .setTitle("Confirmar pago en tienda")
            .setMessage("¿Confirmas que deseas pagar en la tienda por ${payItems.size} productos?")
            .setPositiveButton("Sí") { _, _ ->
                sendOrderToServer(orderRequest)
                // Elimina los ítems del carrito después de confirmar el pago en tienda
                for (item in payItems) {
                    cartViewModel.removeFromCart(item)
                }
                Toast.makeText(this, "Pago en tienda confirmado.", Toast.LENGTH_SHORT).show()
                finish() // Finaliza la actividad después de confirmar
            }
            .setNegativeButton("No", null)
            .show()
    }
    private fun showAddCreditCardDialog(payItems: List<CartItem>, orderRequest: OrderRequest) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_credit_card, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Añadir tarjeta de crédito")
            .setPositiveButton("Añadir") { _, _ ->
                val cardNumber = dialogView.findViewById<EditText>(R.id.cardNumberEditText).text.toString()
                val cardHolder = dialogView.findViewById<EditText>(R.id.cardHolderNameEditText).text.toString()
                val expiryDate = dialogView.findViewById<EditText>(R.id.expiryDateEditText).text.toString()
                val cvv = dialogView.findViewById<EditText>(R.id.cvvEditText).text.toString()
                val userId = 1 // Obtén el ID del usuario autenticado

                if (cardNumber.isNotEmpty() && cardHolder.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                    val creditCard = CreditCard(userId, cardHolder, cardNumber, expiryDate, cvv)

                    // Crea una instancia de CreditCardRepository
                    val creditCardRepository = CreditCardRepository()

                    // Llama al método addCreditCard
                    creditCardRepository.addCreditCard(creditCard, {
                        // Lógica en caso de éxito
                        processCreditCardPayment(payItems, orderRequest)
                        Toast.makeText(this, "Tarjeta añadida y pago exitoso.", Toast.LENGTH_SHORT).show()
                    }, { errorMessage ->
                        // Manejo de errores
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }




    // Función de inicio para realizar el pago
    private fun initiateCreditCardPayment(payItems: List<CartItem>, orderRequest: OrderRequest) {
        // Llama al diálogo de agregar tarjeta
        showAddCreditCardDialog(payItems, orderRequest)
    }

    // Función de proceso de pago
    private fun processCreditCardPayment(payItems: List<CartItem>, orderRequest: OrderRequest) {
        // Lógica para el pago con tarjeta de crédito
        AlertDialog.Builder(this)
            .setTitle("Confirmar pago con tarjeta")
            .setMessage("¿Confirmas el pago con tarjeta por ${payItems.size} productos?")
            .setPositiveButton("Sí") { _, _ ->
                sendOrderToServer(orderRequest)
                // Elimina los ítems del carrito después del pago con tarjeta
                for (item in payItems) {
                    cartViewModel.removeFromCart(item)
                }
                Toast.makeText(this, "Pago con tarjeta exitoso.", Toast.LENGTH_SHORT).show()
                finish() // Finaliza la actividad después de confirmar
            }
            .setNegativeButton("No", null)
            .show()
    }

}
