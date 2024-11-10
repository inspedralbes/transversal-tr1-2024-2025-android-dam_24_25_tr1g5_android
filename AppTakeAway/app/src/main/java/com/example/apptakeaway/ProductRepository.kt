import com.example.apptakeaway.api.RetrofitClient // Importa el cliente Retrofit para acceder a la API
import com.example.apptakeaway.model.Product // Importa el modelo de datos Product
import retrofit2.Call // Importa la interfaz Call para las peticiones API
import retrofit2.Callback // Importa la interfaz Callback para manejar respuestas asíncronas
import retrofit2.Response // Importa la clase Response para representar la respuesta de una petición

/*
 * La clase `ProductRepository` actúa como un intermediario entre la fuente de datos
 * (en este caso, una API) y la lógica de negocio. Se encarga de realizar
 * las peticiones de productos a la API usando Retrofit, manejando la
 * respuesta para retornar los datos o el error correspondiente.
 */
class ProductRepository {
    private val apiService = RetrofitClient.apiService // Inicializa el servicio API usando RetrofitClient

    /*
     * Método para obtener productos de la API. Recibe dos funciones lambda:
     * - onSuccess: se llama con la lista de productos si la petición es exitosa.
     * - onError: se llama con un mensaje de error si la petición falla.
     */
    fun getProducts(onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        apiService.getProducts().enqueue(object : Callback<List<Product>> { // Realiza una llamada asíncrona a la API
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) { // Verifica si la respuesta fue exitosa (código 200)
                    response.body()?.let { products -> // Obtiene la lista de productos del cuerpo de la respuesta
                        onSuccess(products) // Llama a onSuccess con la lista de productos
                    } ?: onError("Respuesta vacía del servidor") // Maneja el caso donde el cuerpo es nulo
                } else {
                    onError("Error: ${response.code()}") // Llama a onError con el código de error
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                onError("Error de xarxa: ${t.message}") // Llama a onError con el mensaje de error de la red
            }
        })
    }
}
