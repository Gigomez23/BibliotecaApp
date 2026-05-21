package ni.edu.uam.bibliotecaapp.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ni.edu.uam.bibliotecaapp.ui.navigation.Screen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        Triple(Screen.Start, "Inicio", Icons.Default.Home),
        Triple(Screen.LibrosList, "Libros", Icons.Default.Book),
        Triple(Screen.AutoresList, "Autores", Icons.Default.Person),
        Triple(Screen.LibroSearch, "Buscar", Icons.Default.Search)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { (screen, label, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Start.route) { StartScreen() }
            composable(Screen.AutoresList.route) { AutoresListScreen(navController) }
            composable(Screen.AutorCreate.route) { AutorDetailScreen(navController) }
            composable(
                Screen.AutorDetail.route,
                arguments = listOf(navArgument("autorId") { type = NavType.LongType })
            ) { backStackEntry ->
                val autorId = backStackEntry.arguments?.getLong("autorId") ?: 0L
                AutorDetailScreen(navController, autorId)
            }
            composable(Screen.LibrosList.route) { LibrosListScreen(navController) }
            composable(Screen.LibroCreate.route) { LibroDetailScreen(navController) }
            composable(
                Screen.LibroDetail.route,
                arguments = listOf(navArgument("libroId") { type = NavType.LongType })
            ) { backStackEntry ->
                val libroId = backStackEntry.arguments?.getLong("libroId") ?: 0L
                LibroDetailScreen(navController, libroId)
            }
            composable(Screen.LibroSearch.route) { LibroSearchScreen(navController) }
        }
    }
}
