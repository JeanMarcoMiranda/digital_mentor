package com.jacket.digital_mentor.presentation.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.OnDeviceTraining
import androidx.compose.material.icons.rounded.PhonelinkRing
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacket.digital_mentor.R
import com.jacket.digital_mentor.core.utils.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val openYouTubeTutorial: () -> Unit = {
        val youtubeUrl = "https://www.youtube.com/watch?v=liIK0G2Wfuw"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)).apply {
            setPackage("com.google.android.youtube")
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp) // Reducido para mejor distribución
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Centrado
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(bottom = 16.dp)
                )
            }

            val buttons = listOf(
                Triple("Soporte en Vivo", Icons.Rounded.SupportAgent) { navController.navigate(Routes.LiveSupport) },
                Triple("Guías de Aprendizaje", Icons.Rounded.LocalLibrary) { navController.navigate(Routes.LearningGuides) },
                Triple("Mi Progreso", Icons.Rounded.OnDeviceTraining) { navController.navigate(Routes.TestResult) },
                Triple("Anexos directos", Icons.Rounded.PhonelinkRing) { navController.navigate(Routes.DirectLinks) },
                Triple("Video Tutoriales", Icons.Rounded.VideoLibrary) { navController.navigate(Routes.VideoTutorials) },
                Triple("Ayuda", Icons.Rounded.DeviceUnknown, openYouTubeTutorial)
            )

            items(buttons) { (text, icon, action) ->
                Button(
                    onClick = action,
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(vertical = 5.dp) // Espaciado más consistente
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = text,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = text,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}