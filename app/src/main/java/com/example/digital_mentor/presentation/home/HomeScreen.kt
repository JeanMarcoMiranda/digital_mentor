package com.example.digital_mentor.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.LocalLibrary
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.OnDeviceTraining
import androidx.compose.material.icons.rounded.PhonelinkRing
import androidx.compose.material.icons.rounded.PsychologyAlt
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                DrawerContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            Screen(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }


}

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
//    Icon(
//        painter = painterResource(id = R.drawable.logo),
//        contentDescription = null,
//        tint = Color.Unspecified,
//        modifier = Modifier
//            .size(150.dp)
//            .padding(16.dp),
//    )

    Text(
        text = "Digital Mentor",
        fontSize = 24.sp,
        modifier = Modifier.padding(16.dp)
    )

    Divider()
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.SupportAgent,
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
                contentDescription = "Notifications"
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
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Digital Mentor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(27.dp)
                    .clickable {
                        onMenuClick()
                    },
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(27.dp),
            )
        }
    )
}