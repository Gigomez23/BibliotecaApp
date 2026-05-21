package ni.edu.uam.bibliotecaapp.data.model

data class Libro(
    val id: Long? = null,
    val titulo: String = "",
    val genero: String = "",
    val anioPublicacion: Int = 0,
    val fechaCreacion: String? = null,
    val disponible: Boolean = true,
    val autor: Autor? = null
)
