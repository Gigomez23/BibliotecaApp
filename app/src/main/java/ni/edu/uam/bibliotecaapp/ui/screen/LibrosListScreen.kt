package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.bibliotecaapp.data.model.Libro
import ni.edu.uam.bibliotecaapp.ui.navigation.Screen
import ni.edu.uam.bibliotecaapp.viewmodel.LibroUiState
import ni.edu.uam.bibliotecaapp.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrosListScreen(
    navController: NavController,
    viewModel: LibroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showOnlyAvailable by remember { mutableStateOf(false) }

    // Refresh data when entering the screen
    LaunchedEffect(Unit) {
        if (showOnlyAvailable) viewModel.cargarLibrosDisponibles() else viewModel.cargarLibros()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca de Libros", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text("Disponibles", style = MaterialTheme.typography.bodySmall)
                        Switch(
                            checked = showOnlyAvailable,
                            onCheckedChange = { 
                                showOnlyAvailable = it 
                                if (it) viewModel.cargarLibrosDisponibles() else viewModel.cargarLibros()
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.LibroCreate.route) },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Libro")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
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
                            Icon(
                                Icons.Default.FilterList, 
                                contentDescription = null, 
                                modifier = Modifier.size(64.dp), 
                                tint = Color.Gray
                            )
                            Text("No hay libros que coincidan.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(8.dp)) {
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

@Composable
fun LibroItem(
    libro: Libro,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(
            containerColor = if (libro.disponible) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = libro.titulo, 
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Autor: ${libro.autor?.nombre ?: "Desconocido"}", 
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Género: ${libro.genero}", 
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Badge(
                    containerColor = if (libro.disponible) Color(0xFF4CAF50) else Color(0xFFF44336),
                    contentColor = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(if (libro.disponible) "Disponible" else "No disponible")
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
