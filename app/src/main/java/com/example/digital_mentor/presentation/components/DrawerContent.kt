package com.example.digital_mentor.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.OnDeviceTraining
import androidx.compose.material.icons.rounded.PhonelinkRing
import androidx.compose.material.icons.rounded.PsychologyAlt
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerContent(modifier: Modifier = Modifier, onLogoutClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Título del drawer
            Text(
                text = "Digital Mentor",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            Divider()

            // Contenido del drawer
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.SupportAgent,
                        contentDescription = "Soporte en vivo"
                    )
                },
                label = {
                    Text(
                        text = "Soporte en vivo",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.VideoLibrary,
                        contentDescription = "Video tutoriales"
                    )
                },
                label = {
                    Text(
                        text = "Video tutoriales",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.LocalLibrary,
                        contentDescription = "Guias de aprendizaje"
                    )
                },
                label = {
                    Text(
                        text = "Guias de aprendizaje",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PsychologyAlt,
                        contentDescription = "Mis consultas"
                    )
                },
                label = {
                    Text(
                        text = "Mis consultas",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.OnDeviceTraining,
                        contentDescription = "Mi progreso"
                    )
                },
                label = {
                    Text(
                        text = "Mi progreso",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.PhonelinkRing,
                        contentDescription = "Anexos directos"
                    )
                },
                label = {
                    Text(
                        text = "Anexos directos",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.DeviceUnknown,
                        contentDescription = "Ayuda"
                    )
                },
                label = {
                    Text(
                        text = "Ayuda",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                selected = false,
                onClick = {}
            )

            // Espacio flexible para empujar el botón de Logout hacia abajo
            Spacer(modifier = Modifier.weight(1f))
        }

        Divider()

        // Opción de Logout
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onLogoutClick),
            color = Color(0xFFE40505), // Usar un rojo más suave (FireBrick)
            shape = MaterialTheme.shapes.medium,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Logout,
                    contentDescription = "Logout",
                    tint = Color.White // Cambiar el color del ícono
                )
                Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre ícono y texto
                Text(
                    text = "Logout",
                    fontSize = 17.sp,
                    color = Color.White // Cambiar el color del texto
                )
            }
        }
    }
}