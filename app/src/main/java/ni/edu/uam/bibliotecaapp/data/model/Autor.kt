package ni.edu.uam.bibliotecaapp.data.model

data class Autor(
    val id: Long? = null,
    val nombre: String = "",
    val nacionalidad: String = "",
    val fechaCreacion: String? = null,
    val libros: List<Libro>? = null,
)
