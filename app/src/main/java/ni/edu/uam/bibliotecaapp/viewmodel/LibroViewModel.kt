package ni.edu.uam.bibliotecaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.bibliotecaapp.data.model.Libro
import ni.edu.uam.bibliotecaapp.data.remote.RetrofitClient

sealed class LibroUiState {
    object Loading: LibroUiState()
    data class Success(val data: List<Libro>) : LibroUiState()
    data class Error(val mensaje: String) : LibroUiState()
}

class LibroViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LibroUiState>(LibroUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            try {
                val libros = RetrofitClient.api_libro.getLibros()
                _uiState.value = LibroUiState.Success(libros)
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al cargar los libros: ${e.message}"
                )
            }
        }
    }

    fun cargarLibrosDisponibles() {
        viewModelScope.launch {
            try {
                val libros = RetrofitClient.api_libro.getLibrosDisponibles()
                _uiState.value = LibroUiState.Success(libros)
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al cargar los libros disponibles: ${e.message}"
                )
            }
        }
    }

    private fun buscarLibro(id: Long) {
        viewModelScope.launch {
            try {
                val libro = RetrofitClient.api_libro.getLibroById(id)
                _uiState.value = LibroUiState.Success(listOf(libro))
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al buscar el libro: ${e.message}"
                )
            }
        }
    }

    fun crearLibro(libro: Libro) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_libro.createLibro(libro)
                cargarLibros()
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al crear el libro: ${e.message}"
                )
            }
        }
    }

    fun actualizarLibro(id: Long, libro: Libro) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_libro.updateLibro(id, libro)
                cargarLibros()
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al actualizar el libro: ${e.message}"
                )
            }
        }
    }

    fun eliminarLibro(id: Long) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_libro.deleteLibro(id)
                cargarLibros()
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al eliminar el libro: ${e.message}"
                )
            }
        }
    }

    fun buscarLibroPorTitulo(titulo: String) {
        viewModelScope.launch {
            try {
                val libros = RetrofitClient.api_libro.buscarLibroPorTitulo(titulo)
                _uiState.value = LibroUiState.Success(libros)
            } catch (e: Exception) {
                _uiState.value = LibroUiState.Error(
                    "Error al buscar libros por título: ${e.message}"
                )
            }
        }
    }
}