package com.example.digital_mentor.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color(0xFF333333),
            fontSize = 14.sp,  // Fuente más pequeña para compactar
            lineHeight = 18.sp
        ),
        placeholder = {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,  // Fuente compacta para el placeholder
                    color = Color(0xFF9E9E9E),
                    lineHeight = 18.sp
                )
            )
        },
        trailingIcon = {
            if (isPassword) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = Color(0xFF6E6E6E),
                    modifier = Modifier
                        .size(18.dp)  // Icono más pequeño para alinearse con el campo compacto
                        .clickable { onPasswordVisibilityChange() }
                )
            }
        },
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)  // Menos padding para compactar
            .height(48.dp),  // Altura reducida para un diseño más compacto
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