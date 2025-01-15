package com.example.digital_mentor.presentation.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.core.utils.AppInfo
import com.example.digital_mentor.core.utils.appList
import com.example.digital_mentor.core.utils.emergencyContacts

@Composable
fun DirectLinksScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit // Callback para la navegación
) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp), // Deja espacio para el botón
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sección de Contactos de Emergencia
            item {
                SectionTitle(title = "Contactos de Emergencia")
            }
            items(emergencyContacts) { (name, phoneNumber) ->
                EmergencyContactTile(
                    name = name,
                    onClick = { openDialer(context, phoneNumber) }
                )
            }

            // Espaciador entre secciones
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sección de Aplicaciones
            item {
                SectionTitle(title = "Aplicaciones")
            }
            items(appList) { app ->
                AppTile(
                    appInfo = app,
                    onClick = { packageName -> openApp(context, packageName) }
                )
            }
        }

        // Botón fijo en la parte inferior
        Button(
            onClick = onNavigateToHome,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50) // Botón con esquinas redondeadas
        ) {
            Text(
                text = "Ir al inicio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
fun EmergencyContactTile(name: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_phone_call),
                    contentDescription = name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Llamar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AppTile(appInfo: AppInfo, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(appInfo.packageName) }
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Cambiar color de fondo
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = appInfo.iconRes),
                    contentDescription = appInfo.name,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = appInfo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Abrir App",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


fun openDialer(context: Context, phoneNumber: String) {
    val cleanNumber = phoneNumber.trim() // Limpia espacios en blanco
    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$cleanNumber")
    }

    try {
        context.startActivity(dialIntent)
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Ocurrió un error al intentar abrir el marcador",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun openApp(context: Context, packageName: String) {
    val packageManager = context.packageManager
    try {
        val appIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (appIntent != null) {
            context.startActivity(appIntent)
        } else {
//            Toast.makeText(context, "La aplicación no está instalada", Toast.LENGTH_SHORT).show()
            redirectToPlayStore(context, packageName)
        }
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir la aplicación", Toast.LENGTH_SHORT).show()
    }
}

fun redirectToPlayStore(context: Context, packageName: String) {
    try {
        val playStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName")
        )
        context.startActivity(playStoreIntent)
    } catch (e: ActivityNotFoundException) {
        // En caso de que no haya Play Store (casos raros), usa un navegador
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        )
        context.startActivity(browserIntent)
    }
}

