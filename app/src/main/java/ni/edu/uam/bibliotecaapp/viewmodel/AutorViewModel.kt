package ni.edu.uam.bibliotecaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.bibliotecaapp.data.model.Autor
import ni.edu.uam.bibliotecaapp.data.model.Libro
import ni.edu.uam.bibliotecaapp.data.remote.RetrofitClient

sealed class AutorUiState {
    object Loading: AutorUiState()
    data class Success(val data: List<Autor>) : AutorUiState()
    data class Error(val message: String) : AutorUiState()
}

class AutorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AutorUiState>(AutorUiState.Loading)
    val uiState = _uiState.asStateFlow()

     init {
          cargarAutores()
     }

     fun cargarAutores() {
         viewModelScope.launch {
             try {
                 val autores = RetrofitClient.api_autor.getAutores()
                 _uiState.value = AutorUiState.Success(autores)
             } catch (e: Exception) {
                 _uiState.value = AutorUiState.Error("Error al cargar los autores: ${e.message}")
             }
         }
     }

    fun buscarAutor(id: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = AutorUiState.Loading
                val autor = RetrofitClient.api_autor.getAutorById(id)
                // We keep the single autor in the success state for simplicity in the detail screen
                _uiState.value = AutorUiState.Success(listOf(autor))
            } catch (e: Exception) {
                _uiState.value = AutorUiState.Error(
                    "Error al buscar el autor: ${e.message}"
                )
            }
        }
    }

    fun crearAutor(autor: Autor) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_autor.createAutor(autor)
                cargarAutores()
            } catch (e: Exception) {
                _uiState.value = AutorUiState.Error(
                    "Error al crear el autor: ${e.message}"
                )
            }
        }
    }

    fun actualizarAutor(id: Long, autor: Autor) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_autor.updateAutor(id, autor)
                cargarAutores()
            } catch (e: Exception) {
                _uiState.value = AutorUiState.Error(
                    "Error al actualizar el autor: ${e.message}"
                )
            }
        }
    }

    fun eliminarAutor(id: Long) {
        viewModelScope.launch {
            try {
                RetrofitClient.api_autor.deleteAutor(id)
                cargarAutores()
            } catch (e: Exception) {
                _uiState.value = AutorUiState.Error(
                    "Error al eliminar el autor: ${e.message}"
                )
            }
        }
    }
}

