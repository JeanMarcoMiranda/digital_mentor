package com.example.digital_mentor.presentation.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.digital_mentor.core.utils.AppInfo
import com.example.digital_mentor.core.utils.appList

@Composable
fun DirectLinksScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AppsGridList(
        modifier = modifier,
        apps = appList,
        onAppClick = { packageName ->
            openApp(context, packageName)
        }
    )

//    Column(
//        modifier = modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("Links directos")
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                openApp(context, "com.google.android.youtube")
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Icon(
//                imageVector = Icons.Default.PlayArrow, // Puedes usar cualquier ícono que prefieras
//                contentDescription = "Abrir YouTube"
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text("Abrir YouTube")
//        }
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsGridList(modifier: Modifier = Modifier, apps: List<AppInfo>, onAppClick: (String) -> Unit) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            items(apps) { app ->
                AppGridItem(appInfo = app, onClick = onAppClick)
            }
        }
    )
}

@Composable
fun AppGridItem(appInfo: AppInfo, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(appInfo.packageName) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = appInfo.iconRes),
            contentDescription = appInfo.name,
            modifier = Modifier
                .size(64.dp)
//                .clip(CircleShape)
//                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = appInfo.name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}


fun openApp(context: Context, packageName: String) {
    val packageManager = context.packageManager
    try {
        val appIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (appIntent != null) {
            context.startActivity(appIntent)
        } else {
            Toast.makeText(context, "La aplicación no está instalada", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir la aplicación", Toast.LENGTH_SHORT).show()
    }
}

