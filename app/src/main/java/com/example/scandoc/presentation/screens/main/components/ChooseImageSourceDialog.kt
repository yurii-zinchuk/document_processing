package com.example.scandoc.presentation.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ChooseImageSourceDialog(
    onDismiss: () -> Unit,
    onGallery: () -> Unit,
    onFiles: () -> Unit,
    onDirectory: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Image Source") },
        text = {
            Column {
                OptionButton(
                    "From Gallery",
                    onClick = {
                        onGallery()
                        onDismiss()
                    }
                )
                OptionButton(
                    "From Files",
                    onClick = {
                        onFiles()
                        onDismiss()
                    }
                )
                OptionButton(
                    "Choose Directory",
                    onClick = {
                        onDirectory()
                        onDismiss()
                    }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun OptionButton(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, textAlign = TextAlign.Start)
    }
}
