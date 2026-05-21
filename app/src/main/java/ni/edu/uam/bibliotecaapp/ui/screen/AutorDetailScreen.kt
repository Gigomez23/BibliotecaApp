package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ni.edu.uam.bibliotecaapp.data.model.Autor
import ni.edu.uam.bibliotecaapp.viewmodel.AutorViewModel
import ni.edu.uam.bibliotecaapp.viewmodel.AutorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutorDetailScreen(
    navController: NavController,
    autorId: Long = 0L,
    viewModel: AutorViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    val isEditing = autorId != 0L
    
    // Fetch specific author details to get the full list of books
    LaunchedEffect(autorId) {
        if (isEditing) {
            viewModel.buscarAutor(autorId)
        }
    }
    
    // We try to find the author in the current list
    val autorToEdit = (uiState as? AutorUiState.Success)?.data?.find { it.id == autorId }
    
    LaunchedEffect(autorToEdit) {
        autorToEdit?.let {
            nombre = it.nombre
            nacionalidad = it.nacionalidad
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (!isEditing) "Nuevo Autor" else "Detalles del Autor", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { 
                        viewModel.cargarAutores() // Ensure list is full when going back
                        navController.popBackStack() 
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Person, 
                    contentDescription = null, 
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = nacionalidad,
                    onValueChange = { nacionalidad = it },
                    label = { Text("Nacionalidad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                
                Button(
                    onClick = {
                        val autor = Autor(
                            id = if (isEditing) autorId else null, 
                            nombre = nombre, 
                            nacionalidad = nacionalidad
                        )
                        if (!isEditing) {
                            viewModel.crearAutor(autor)
                        } else {
                            viewModel.actualizarAutor(autorId, autor)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(if (!isEditing) "Registrar Autor" else "Actualizar Información", fontSize = 18.sp)
                }

                if (isEditing && autorToEdit?.libros != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Libros Publicados", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (autorToEdit.libros.isEmpty()) {
                        Text("Este autor aún no tiene libros registrados.", modifier = Modifier.padding(vertical = 16.dp))
                    } else {
                        autorToEdit.libros.forEach { libro ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(libro.titulo, fontWeight = FontWeight.SemiBold)
                                        Text("${libro.genero} (${libro.anioPublicacion})", style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
