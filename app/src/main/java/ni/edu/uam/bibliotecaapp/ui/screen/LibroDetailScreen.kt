package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
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
import ni.edu.uam.bibliotecaapp.data.model.Libro
import ni.edu.uam.bibliotecaapp.viewmodel.LibroViewModel
import ni.edu.uam.bibliotecaapp.viewmodel.LibroUiState
import ni.edu.uam.bibliotecaapp.viewmodel.AutorViewModel
import ni.edu.uam.bibliotecaapp.viewmodel.AutorUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroDetailScreen(
    navController: NavController,
    libroId: Long = 0L,
    viewModel: LibroViewModel = viewModel(),
    autorViewModel: AutorViewModel = viewModel()
) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf(true) }
    var selectedAutor by remember { mutableStateOf<Autor?>(null) }
    
    val uiState by viewModel.uiState.collectAsState()
    val autorUiState by autorViewModel.uiState.collectAsState()

    val isEditing = libroId != 0L
    val libroToEdit = (uiState as? LibroUiState.Success)?.data?.find { it.id == libroId }
    
    LaunchedEffect(libroToEdit) {
        libroToEdit?.let {
            titulo = it.titulo
            genero = it.genero
            anio = it.anioPublicacion.toString()
            disponible = it.disponible
            selectedAutor = it.autor
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (!isEditing) "Nuevo Libro" else "Editar Libro", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Book, 
                contentDescription = null, 
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            OutlinedTextField(
                value = anio,
                onValueChange = { anio = it },
                label = { Text("Año de Publicación") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(), 
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Disponible para préstamo", fontWeight = FontWeight.Medium)
                    Switch(checked = disponible, onCheckedChange = { disponible = it })
                }
            }
            
            Text("Seleccionar Autor", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
            
            when (val state = autorUiState) {
                is AutorUiState.Success -> {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedAutor?.nombre ?: "Seleccione un autor",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.data.forEach { autor ->
                                DropdownMenuItem(
                                    text = { Text(autor.nombre) },
                                    onClick = {
                                        selectedAutor = autor
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                else -> CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = {
                    val libro = Libro(
                        id = if (isEditing) libroId else null,
                        titulo = titulo,
                        genero = genero,
                        anioPublicacion = anio.toIntOrNull() ?: 0,
                        disponible = disponible,
                        autor = selectedAutor
                    )
                    if (!isEditing) {
                        viewModel.crearLibro(libro)
                    } else {
                        viewModel.actualizarLibro(libroId, libro)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = selectedAutor != null && titulo.isNotBlank(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (!isEditing) "Guardar Libro" else "Actualizar Libro", fontSize = 18.sp)
            }
        }
    }
}
