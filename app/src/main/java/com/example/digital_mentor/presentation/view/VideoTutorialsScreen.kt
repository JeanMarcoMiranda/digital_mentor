package com.example.digital_mentor.presentation.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.digital_mentor.domain.model.Video
import com.example.digital_mentor.presentation.intent.VideoTutorialsIntent
import com.example.digital_mentor.presentation.intent.VideoTutorialsState
import com.example.digital_mentor.presentation.viewmodel.VideoTutorialsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VideoTutorialsScreen(
    modifier: Modifier = Modifier,
    viewModel: VideoTutorialsViewModel = koinViewModel()
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
            is VideoTutorialsState.VideoSelection -> {
                val filterVideos = state.videos.filter {
                    it.title.contains(state.searchQuery, ignoreCase = true)
                }

                // Title
                Text(
                    text = "Video Tutoriales",
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                // SearchBar
                SearchBar(
                    query = (viewState as? VideoTutorialsState.VideoSelection)?.searchQuery ?: "",
                    onQueryChange = {
                        viewModel.sendIntent(VideoTutorialsIntent.onSearchTextChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                VideoGridContent(
                    videos = filterVideos,
                    onVideoClick = { video ->
                        viewModel.sendIntent(VideoTutorialsIntent.onVideoSelected(video))
                    })
            }

            is VideoTutorialsState.Error -> Text(
                state.message,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            VideoTutorialsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is VideoTutorialsState.VideoDetail -> {
                VideoDetailContent(
                    video = state.selectedVideo,
                    onVideoTutorialClick = {
                        viewModel.sendIntent(VideoTutorialsIntent.onVideoTutorialsClicked)
                    }
                )
            }
        }
    }
}

@Composable
fun VideoGridContent(
    videos: List<Video>,
    onVideoClick: (selectedVideo: Video) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(videos) { video ->
            VideoCard(video = video, onVideoClick = onVideoClick)
        }
    }
}

@Composable
fun VideoCard(
    video: Video,
    onVideoClick: (selectedVideo: Video) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onVideoClick(video) }
    ) {
        val dynamicHeight = maxHeight * 0.4f // Ajusta el valor según sea necesario

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            modifier = Modifier.heightIn(min = 200.dp, max = dynamicHeight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = video.youtubeVideoImage,
                    contentDescription = video.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = video.title,
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
fun VideoDetailContent(
    modifier: Modifier = Modifier,
    video: Video,
    onVideoTutorialClick: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            // Title
            Text(
                text = "Detalles del Video",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }

        item {
            // Video Title
            Text(
                text = video.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 15.dp, bottom = 8.dp)
            )
        }

        item {
            // Video Image
            AsyncImage(
                model = video.youtubeVideoImage,
                contentDescription = "Imagen de ${video.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f) // Ajuste para videos
                    .clip(RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))

            // Video Description
            Text(
                text = video.description ?: "Esta guía no cuenta con una descripción",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            // Botones en la parte inferior
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        openYouTubeVideo(context, video.youtubeVideoId)
                    },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(text = "Ver Video", fontSize = 20.sp)
                }

                Button(
                    onClick = onVideoTutorialClick,
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
}


/**
 * Abre un video de YouTube. Si la aplicación de YouTube está instalada, la usa;
 * de lo contrario, abre el video en un navegador.
 */
fun openYouTubeVideo(context: Context, videoId: String) {
    val youtubeAppPackage = "com.google.android.youtube"
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
    val webIntent =
        Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))

    try {
        // Verifica si la app de YouTube está instalada
        if (isAppInstalled(context, youtubeAppPackage)) {
            context.startActivity(appIntent)
        } else {
            // Si no está instalada, abre en el navegador
            context.startActivity(webIntent)
        }
    } catch (e: Exception) {
        // En caso de error, usa el navegador como fallback
        context.startActivity(webIntent)
    }
}

/**
 * Verifica si una aplicación está instalada en el dispositivo.
 */
fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}