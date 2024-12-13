package com.example.digital_mentor.presentation.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun VideoTutorialsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val videoId = "WT9-4DXUqsM" // Reemplaza con el ID del video de YouTube

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { openYouTubeVideo(context, videoId) }) {
            Text("Abrir Video Tutorial")
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
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))

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