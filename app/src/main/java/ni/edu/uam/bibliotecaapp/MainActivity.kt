package ni.edu.uam.bibliotecaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ni.edu.uam.bibliotecaapp.ui.screen.MainScreen
import ni.edu.uam.bibliotecaapp.ui.theme.BibliotecaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliotecaAppTheme {
                MainScreen()
            }
        }
    }
}
