package com.example.digital_mentor.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.presentation.components.CustomCardTextField
import com.example.digital_mentor.presentation.intent.UserProfileIntent
import com.example.digital_mentor.presentation.intent.UserProfileState
import com.example.digital_mentor.presentation.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = koinViewModel(), // Pasamos el ViewModel
    onNavBack: () -> Unit, // Callback para manejar la navegación
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if (viewState is UserProfileState.UserData && (viewState as UserProfileState.UserData).isEditSheetOpen) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    viewModel.sendIntent(UserProfileIntent.CloseEditCard)
                }
            }
        ) {
            CardEditForm(
                viewState = viewState,
                onCardNumberChange = {
                    viewModel.sendIntent(
                        UserProfileIntent.UpdateCardDetails(
                            it,
                            (viewState as UserProfileState.UserData).expirationDate,
                            (viewState as UserProfileState.UserData).cvv,
                        )
                    )
                },
                onExpirationDateChange = {
                    viewModel.sendIntent(
                        UserProfileIntent.UpdateCardDetails(
                            (viewState as UserProfileState.UserData).cardNumber,
                            it,
                            (viewState as UserProfileState.UserData).cvv,
                        )
                    )
                },
                onCvvChange = {
                    viewModel.sendIntent(
                        UserProfileIntent.UpdateCardDetails(
                            (viewState as UserProfileState.UserData).cardNumber,
                            (viewState as UserProfileState.UserData).expirationDate,
                            it
                        )
                    )
                },
                onSave = {
                    coroutineScope.launch {
                        viewModel.sendIntent(UserProfileIntent.SaveCardDetails)
                        bottomSheetState.hide()
                    }
                }
            )
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Título de la pantalla
        Text(
            text = "Mi perfil",
            modifier = Modifier.padding(vertical = 16.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))
        // Espacio para la imagen de usuario
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤", // Placeholder para la imagen
                color = MaterialTheme.colorScheme.primary,
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        when (viewState) {
            is UserProfileState.Loading -> {
                CircularProgressIndicator()
            }

            is UserProfileState.UserData -> {
                val userProfile = (viewState as UserProfileState.UserData).userProfile
                val email = (viewState as UserProfileState.UserData).email
                if (userProfile != null) {
                    UserProfileDetails(
                        name = userProfile.name,
                        email = email, // Supongo que necesitas agregar esto como dato
                        phone = userProfile.phoneNumber // Agregar teléfono si está en tu modelo
                    )
                } else {
                    Text("No hay datos disponibles")
                }
            }

            else -> Unit
        }

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.sendIntent(UserProfileIntent.OpenEditCard)
                    bottomSheetState.show()
                }
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text(
                "Editar datos de la tarjeta",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onNavBack,
            modifier = Modifier.width(300.dp)
        ) {
            Text(
                "Ir al inicio",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }


    // Show toast messages based on the current state
    LaunchedEffect(viewState) {
        if (viewState is UserProfileState.UserData) {
            val state = viewState as UserProfileState.UserData
            state.successMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
            state.errorMessage?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        }
    }
}

@Composable
fun UserProfileDetails(
    name: String?,
    email: String?,
    phone: String?
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        UserProfileRow(label = "Nombre:", value = name)
        UserProfileRow(label = "Correo:", value = email)
        UserProfileRow(label = "Teléfono:", value = phone)
    }
}

@Composable
fun UserProfileRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = value ?: "N/A",
            modifier = Modifier.weight(1f),
            fontSize = 20.sp
        )
    }
}

@Composable
fun CardEditForm(
    viewState: UserProfileState,
    onCardNumberChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    onSave: () -> Unit
) {
    if (viewState is UserProfileState.UserData) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Editar información de tarjeta",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Número de tarjeta ocupa dos columnas
                item(span = { GridItemSpan(2) }) {
                    OutlinedTextField(
                        value = viewState.cardNumber ?: "",
                        onValueChange = onCardNumberChange,
                        label = { Text("Número de tarjeta") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    CustomCardTextField(
                        value = viewState.cardNumber ?: "",
                        error = viewState.cardError,
                        onValueChange = {
                            if (it.length <= 16) {
                                onCardNumberChange(it)
                            }
                        },
                        label = "Número de tarjetaa",
                        placeholder = "#### #### #### ####",
                    )
                }

                // Fecha de vencimiento ocupa una columna
                item {
                    OutlinedTextField(
                        value = viewState.expirationDate ?: "",
                        onValueChange = onExpirationDateChange,
                        label = { Text("FV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // CVV ocupa una columna
                item {
                    OutlinedTextField(
                        value = viewState.cvv ?: "",
                        onValueChange = {
                            if (it.length <= 3) {
                                onCvvChange(it)
                            }
                        },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
            ) {
                Text(
                    "Guardar",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }
    }
}