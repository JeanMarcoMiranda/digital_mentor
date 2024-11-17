package com.example.digital_mentor.presentation.view

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
                    onLearningGuideClick = {}
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
            .padding(bottom = 15.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(courses) { course ->
            CourseCard(course = course, onCourseClick = onCourseClick)
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    onCourseClick: (selectedCourse: Course) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        ),
        modifier = Modifier
            .height(250.dp)
            .clickable {
                onCourseClick(course)
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = course.image,
                contentDescription = course.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = course.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
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
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Nombre del curso
        Text(
            text = course.name,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
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
                Text(text = "Leer")
            }

            Button(
                onClick = onLearningGuideClick,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(start = 8.dp)
            ) {
                Text(text = "Guía de Aprendizaje", textAlign = TextAlign.Center)
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
        Toast.makeText(context, "Error al descargar el archivo: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}