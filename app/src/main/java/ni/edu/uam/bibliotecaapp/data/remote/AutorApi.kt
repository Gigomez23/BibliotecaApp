package ni.edu.uam.bibliotecaapp.data.remote

import ni.edu.uam.bibliotecaapp.data.model.Autor
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AutorApi {
    @GET("api/autores")
    suspend fun getAutores(): List<Autor>

    @GET("api/autores/{id}")
    suspend fun getAutorById(@Path("id") id: Long): Autor

    @POST("api/autores")
    suspend fun createAutor(@Body autor: Autor): Autor

    @PUT("api/autores/{id}")
    suspend fun updateAutor(@Path("id") id: Long, @Body autor: Autor): Autor

    @DELETE("api/autores/{id}")
    suspend fun deleteAutor(@Path("id") id: Long)
}
