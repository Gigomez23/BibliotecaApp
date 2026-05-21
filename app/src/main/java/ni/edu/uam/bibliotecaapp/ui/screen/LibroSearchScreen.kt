package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.bibliotecaapp.ui.navigation.Screen
import ni.edu.uam.bibliotecaapp.viewmodel.LibroUiState
import ni.edu.uam.bibliotecaapp.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroSearchScreen(
    navController: NavController,
    viewModel: LibroViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscador de Libros", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { 
                        query = it
                        if (it.length >= 3) {
                            viewModel.buscarLibroPorTitulo(it)
                        } else if (it.isEmpty()) {
                            viewModel.cargarLibros()
                        }
                    },
                    placeholder = { Text("Escribe el título...") },
                    label = { Text("Buscar por Título") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(modifier = Modifier.weight(1f)) {
                when (val state = uiState) {
                    is LibroUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is LibroUiState.Success -> {
                        if (state.data.isEmpty()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No se encontraron resultados para \"$query\"", color = Color.Gray)
                            }
                        } else {
                            LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                                items(state.data) { libro ->
                                    LibroItem(
                                        libro = libro,
                                        onEdit = { navController.navigate(Screen.LibroDetail.createRoute(libro.id ?: 0L)) },
                                        onDelete = { libro.id?.let { viewModel.eliminarLibro(it) } }
                                    )
                                }
                            }
                        }
                    }
                    is LibroUiState.Error -> {
                        Text("Error: ${state.mensaje}", color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}
