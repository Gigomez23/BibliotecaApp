# BibliotecaApp 📚

BibliotecaApp es una aplicación Android moderna diseñada para la gestión eficiente de libros y autores. Desarrollada como parte del curso de **Programación Orientada a Objetos II (POO II)** en la **UAM**, la aplicación implementa las mejores prácticas de desarrollo móvil con un enfoque en la arquitectura MVVM y el diseño Material 3.

## 🚀 Características Principales

### ✍️ Gestión de Autores
* **Listado Completo:** Visualización de todos los autores registrados en el sistema.
* **CRUD Operaciones:** Capacidad para registrar nuevos autores, editar información existente y eliminar registros.
* **Interfaz Intuitiva:** Identificación visual rápida mediante avatares generados dinámicamente.

### 📖 Gestión de Libros
* **Inventario Detallado:** Lista de libros con detalles sobre género, año de publicación y autor asociado.
* **Filtros Avanzados:** Interruptor para filtrar instantáneamente entre todos los libros o solo aquellos disponibles para préstamo.
* **Búsqueda en Tiempo Real:** Buscador por título que filtra resultados mientras el usuario escribe.
* **Estado de Disponibilidad:** Indicadores visuales claros (verde/rojo) para conocer el estado de cada ejemplar.

### 🎨 Diseño y UX
* **Material 3:** Implementación de la última guía de diseño de Google.
* **Navegación Fluida:** Menú inferior para cambiar rápidamente entre las secciones principales (Inicio, Libros, Autores, Buscar).
* **Temas Coloridos:** Uso de esquemas de color diferenciados para cada sección para mejorar la orientación del usuario.

## 🛠️ Stack Tecnológico

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Arquitectura declarativa)
* **Red:** [Retrofit 2](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson) para consumo de APIs REST.
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Navegación:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation).
* **Asincronía:** Kotlin Coroutines & StateFlow para reactividad de datos.

## 📂 Estructura del Proyecto

```text
ni.edu.uam.bibliotecaapp/
├── data/
│   ├── model/       # Modelos de datos (Libro, Autor)
│   └── remote/      # Interfaces de API y Cliente Retrofit
├── ui/
│   ├── navigation/  # Configuración de rutas y navegación
│   ├── screen/      # Pantallas de la aplicación (Composables)
│   └── theme/       # Configuración de colores y estilos (Material 3)
└── viewmodel/       # Lógica de negocio y gestión de estado de la UI
```

## ⚙️ Configuración y Requisitos

### Requisitos Previos
* Android Studio Ladybug (o superior).
* JDK 17 o superior.
* Dispositivo Android con API 24 (Nougat) o superior.

### Backend
La aplicación está configurada para conectarse a un servidor local en:
`http://0.0.0.0:8080/`

Para cambiar la URL base, modifique el archivo:
`app/src/main/java/ni/edu/uam/bibliotecaapp/data/remote/RetrofitClient.kt`

## 📝 Documentación de la API

La aplicación consume los siguientes endpoints:

### Autores
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/autores` | Obtener todos los autores |
| POST | `/api/autores` | Crear un nuevo autor |
| PUT | `/api/autores/{id}` | Actualizar un autor |
| DELETE | `/api/autores/{id}` | Eliminar un autor |

### Libros
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/api/libros` | Obtener todos los libros |
| GET | `/api/libros/disponibles` | Obtener libros disponibles |
| GET | `/api/libros/buscar?titulo=...` | Buscar libros por título |
| POST | `/api/libros` | Crear un nuevo libro |
| DELETE | `/api/libros/{id}` | Eliminar un libro |

---
Desarrollado para la **Universidad Americana (UAM)**.
