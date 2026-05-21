package ni.edu.uam.bibliotecaapp.ui.navigation

sealed class Screen(val route: String) {
    object Start : Screen("start")
    object AutoresList : Screen("autores_list")
    object AutorDetail : Screen("autor_detail/{autorId}") {
        fun createRoute(autorId: Long) = "autor_detail/$autorId"
    }
    object AutorCreate : Screen("autor_create")
    object LibrosList : Screen("libros_list")
    object LibroDetail : Screen("libro_detail/{libroId}") {
        fun createRoute(libroId: Long) = "libro_detail/$libroId"
    }
    object LibroCreate : Screen("libro_create")
    object LibroSearch : Screen("libro_search")
}
