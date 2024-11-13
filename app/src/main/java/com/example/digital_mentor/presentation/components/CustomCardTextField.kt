package com.example.digital_mentor.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Column {

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9E9E9E),
                        lineHeight = 18.sp
                    )
                )
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF9E9E9E),
                        lineHeight = 18.sp
                    )
                )
            },
            visualTransformation = CardNumberVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE0F7FA),
                errorContainerColor = Color.Red,
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedTextColor = Color(0xFF333333),
                unfocusedTextColor = Color(0xFF6E6E6E),
                focusedLabelColor = Color(0xFF00796B),
                cursorColor = Color(0xFF00796B)
            ),
            modifier = modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier.padding(start = 10.dp, top = 4.dp)
            )
        }
    }

}

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Dividir el texto en bloques de 4 dígitos y agregar espacios
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        val formatted = trimmed.chunked(4).joinToString(" ")

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // Calcular la nueva posición considerando los espacios agregados
                return if (offset <= 4) offset else offset + (offset - 1) / 4
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Calcular la posición original considerando los espacios
                return if (offset <= 4) offset else offset - (offset / 5)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}