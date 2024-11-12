package com.example.digital_mentor.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CardNumberInput() {
//    var cardNumber by remember { mutableStateOf("") }
//    var isError by remember { mutableStateOf(false) }
//
//    OutlinedTextField(
//        value = cardNumber,
//        onValueChange = {
//            if (it.length <= 16) cardNumber = it
//            isError = cardNumber.length != 16  // Validación básica de longitud
//        },
//        label = { Text("Número de tarjeta") },
//        placeholder = { Text("#### #### #### ####") },
//        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_card), contentDescription = null) },
//        visualTransformation = CardNumberVisualTransformation(),  // Formateo en bloques de 4 dígitos
//        isError = isError,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//        colors = TextFieldDefaults.colors(
//            focusedContainerColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
//            errorBorderColor = Color.Red
//        ),
//        modifier = Modifier.padding(16.dp)
//    )
//}
//
//class CardNumberVisualTransformation : VisualTransformation {
//    override fun filter(text: AnnotatedString): TransformedText {
//        // Aquí formateamos el texto en bloques de 4 dígitos
//        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
//        val formatted = trimmed.chunked(4).joinToString(" ")
//
//        return TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)
//    }
//}