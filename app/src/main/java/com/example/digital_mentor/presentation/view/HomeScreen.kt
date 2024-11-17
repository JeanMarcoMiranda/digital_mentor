package com.example.digital_mentor.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.OnDeviceTraining
import androidx.compose.material.icons.rounded.PhonelinkRing
import androidx.compose.material.icons.rounded.PsychologyAlt
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digital_mentor.R
import com.example.digital_mentor.core.utils.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = modifier.height(40.dp))

        Button(
            onClick = {
                navController.navigate(Routes.LiveSupport)
            },
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.SupportAgent,
                contentDescription = "Soporte en vivo"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Soporte en Vivo", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.navigate(Routes.LearningGuides)
            },
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.LocalLibrary,
                contentDescription = "Guias de Aprendizaje"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Guias de Aprendizaje", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.navigate(Routes.TestResult)
            },
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.OnDeviceTraining,
                contentDescription = "Mi progreso"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Mi Progreso", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(40.dp),
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.PhonelinkRing,
                contentDescription = "Anexos directos"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Anexos directos", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(40.dp),
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.VideoLibrary,
                contentDescription = "Video Tutoriales"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Video Tutoriales", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(40.dp),
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.DeviceUnknown,
                contentDescription = "Ayuda"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Ayuda", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}