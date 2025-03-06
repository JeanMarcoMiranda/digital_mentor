package com.jacket.digital_mentor.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun CustomCardExpirationDateTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            val sanitizedValue = it.filter { char -> char.isDigit() }
            if (sanitizedValue.length <= 4) {
                onValueChange(sanitizedValue)
            }
        },
        label = { Text("FV") },
        placeholder = { Text("MM/YY") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = ExpirationDateVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

class ExpirationDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Filtrar solo los dígitos
        val rawText = text.text.filter { it.isDigit() }

        // Formatear el texto
        val formattedText = buildString {
            if (rawText.length > 0) append(rawText.substring(0, minOf(2, rawText.length)))
            if (rawText.length > 2) append("/").append(rawText.substring(2, rawText.length))
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset > 2 -> offset + 1 // Agregar espacio para el `/`
                    else -> formattedText.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset > 2 -> offset - 1 // Ignorar el `/`
                    else -> rawText.length
                }
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}