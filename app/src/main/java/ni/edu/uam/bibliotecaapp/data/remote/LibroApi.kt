package ni.edu.uam.bibliotecaapp.data.remote

import ni.edu.uam.bibliotecaapp.data.model.Libro
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface LibroApi {
    @GET("api/libros")
    suspend fun getLibros(): List<Libro>

    @GET("api/libros/{id}")
    suspend fun getLibroById(@Path("id") id: Long): Libro

    @GET("api/libros/disponibles")
    suspend fun getLibrosDisponibles(): List<Libro>

    @POST("api/libros")
    suspend fun createLibro(@Body libro: Libro): Libro

    @PUT("api/libros/{id}")
    suspend fun updateLibro(@Path("id") id: Long, @Body libro: Libro): Libro

    @DELETE("api/libros/{id}")
    suspend fun deleteLibro(@Path("id") id: Long)

    @GET("api/libros/buscar")
    suspend fun buscarLibroPorTitulo(@Query("titulo") titulo: String): List<Libro>
}