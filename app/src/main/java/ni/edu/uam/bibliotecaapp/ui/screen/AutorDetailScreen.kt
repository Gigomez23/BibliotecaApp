package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.layout.*
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
                title = { Text(if (!isEditing) "Nuevo Autor" else "Editar Autor", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .fillMaxSize(),
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
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
                Text(if (!isEditing) "Registrar Autor" else "Actualizar Autor", fontSize = 18.sp)
            }
        }
    }
}
