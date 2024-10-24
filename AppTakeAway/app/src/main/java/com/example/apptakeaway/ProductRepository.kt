import com.example.apptakeaway.api.RetrofitClient
import com.example.apptakeaway.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {
    private val apiService = RetrofitClient.apiService

    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        onSuccess(products)
                    } ?: onError("Respuesta vacía del servidor")
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                onError("Error de red: ${t.message}")
            }
        })
    }
}