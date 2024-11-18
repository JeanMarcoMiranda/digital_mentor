package com.example.digital_mentor.presentation.view

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.digital_mentor.domain.model.Course
import com.example.digital_mentor.presentation.intent.LearningGuidesIntent
import com.example.digital_mentor.presentation.intent.LearningGuidesState
import com.example.digital_mentor.presentation.viewmodel.LearningGuidesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LearningGuidesScreen(
    viewModel: LearningGuidesViewModel = koinViewModel(), modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        when (val state = viewState) {
            is LearningGuidesState.CourseSelection -> {
                val filteredCourses = state.courses.filter {
                    it.name.contains(state.searchQuery, ignoreCase = true)
                }
                // Title
                Text(
                    text = "Guías de Aprendizaje",
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                // SearchBar
                SearchBar(
                    query = (viewState as? LearningGuidesState.CourseSelection)?.searchQuery ?: "",
                    onQueryChange = {
                        viewModel.sendIntent(LearningGuidesIntent.OnSearchTextChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                CourseGridContent(
                    courses = filteredCourses,
                    onCourseClick = { course ->
                        viewModel.sendIntent(
                            LearningGuidesIntent.onCourseSelected(
                                course
                            )
                        )
                    }
                )
            }

            is LearningGuidesState.Error -> Text(
                state.message,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            LearningGuidesState.Loding -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is LearningGuidesState.CourseDetail -> {
                CourseDetailContent(
                    course = state.selectedCourse,
                    onReadClick = {},
                    onLearningGuideClick = { viewModel.sendIntent(LearningGuidesIntent.onLearingGuidesClicked) }
                )
            }
        }
    }
}

@Composable
fun CourseGridContent(
    courses: List<Course>,
    onCourseClick: (selectedCourse: Course) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses) { course ->
            CourseCard(course = course, onCourseClick = onCourseClick)
        }
    }
}

//@Composable
//fun CourseCard(
//    course: Course,
//    onCourseClick: (selectedCourse: Course) -> Unit
//) {
//    Card(
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
//        shape = RoundedCornerShape(16.dp),
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 200.dp, max = 250.dp) // Altura uniforme
//            .clickable { onCourseClick(course) }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .padding(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // Imagen del curso
//            AsyncImage(
//                model = course.image,
//                contentDescription = course.name,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f) // Relación de aspecto cuadrada
//                    .clip(RoundedCornerShape(12.dp)) // Bordes redondeados para la imagen
//            )
//
//            // Contenedor de texto con scroll
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f) // Espacio restante para el texto
//                    .verticalScroll(rememberScrollState())
//            ) {
//                Text(
//                    text = course.name,
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(4.dp),
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//            }
//        }
//    }
//}

@Composable
fun CourseCard(
    course: Course,
    onCourseClick: (selectedCourse: Course) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCourseClick(course) }
    ) {
        // Calculamos la altura dinámica
        val dynamicHeight = maxHeight * 0.4f // Ajusta el valor según sea necesario

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            modifier = Modifier
                .heightIn(min = 200.dp, max = dynamicHeight) // Ajustamos el alto dinámicamente
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Imagen del curso
                AsyncImage(
                    model = course.image,
                    contentDescription = course.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f) // Mantener la relación de aspecto
                        .clip(RoundedCornerShape(12.dp)) // Bordes redondeados
                )

                // Contenedor de texto sin weight, para que se ajuste automáticamente al contenido
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp) // Añadir algo de espacio entre la imagen y el texto
                ) {
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(4.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = {
            Text(
                text = "Buscar curso",
                style = TextStyle(
                    fontSize = 14.sp,  // Fuente compacta para el placeholder
                    color = Color(0xFF9E9E9E),
                    lineHeight = 18.sp
                )
            )
        },
        placeholder = {
            Text(
                text = "Busca la lectura de tu interes",
                style = TextStyle(
                    fontSize = 14.sp,  // Fuente compacta para el placeholder
                    color = Color(0xFF9E9E9E),
                    lineHeight = 18.sp
                )
            )
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp), // Bordes redondeados
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE0F7FA),
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedTextColor = Color(0xFF333333),
            unfocusedTextColor = Color(0xFF6E6E6E),
            focusedLabelColor = Color(0xFF00796B),
            cursorColor = Color(0xFF00796B)
        )
    )
}

@Composable
fun CourseDetailContent(
    course: Course,
    onReadClick: () -> Unit,
    onLearningGuideClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Detalles del Curso",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp),
        )

        // Nombre del curso
        Text(
            text = course.name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
        )

        // Imagen del curso con más altura
        AsyncImage(
            model = course.image,
            contentDescription = "Imagen de ${course.name}",
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f) // Dar más peso a la imagen para que ocupe más espacio vertical
                .padding(vertical = 8.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Descripción del curso
        Text(
            text = course.description ?: "Esta guía no cuenta con una descripción",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )

        // Espaciador para empujar los botones hacia abajo
        Spacer(modifier = Modifier.weight(1f))

        // Botones en la parte inferior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    downloadPdf(context, course.pdf, course.name)
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Leer", fontSize = 20.sp)
            }

            Button(
                onClick = onLearningGuideClick,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(start = 8.dp)
            ) {
                Text(text = "Guía de Aprendizaje", textAlign = TextAlign.Center, fontSize = 20.sp)
            }
        }
    }
}

@SuppressLint("ServiceCast")
fun downloadPdf(context: Context, pdfUrl: String, courseName: String) {
    try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(pdfUrl)

        val request = DownloadManager.Request(uri).apply {
            setTitle("Descargando $courseName")
            setDescription("Descargando el archivo PDF del curso.")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$courseName.pdf")
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        downloadManager.enqueue(request)
        Toast.makeText(context, "Descarga iniciada...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error al descargar el archivo: ${e.message}", Toast.LENGTH_SHORT)
            .show()
    }
}