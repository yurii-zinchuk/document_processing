package com.example.scandoc.presentation.screens.main.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.scandoc.R

@Composable
fun ChooseImageSourceDialog(
    onDismiss: () -> Unit,
    onGallery: () -> Unit,
    onFiles: () -> Unit,
    onDirectory: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_images_dialog_title)) },
        text = {
            Column {
                OptionButton(
                    text = stringResource(R.string.select_images_dialog_option_gallery),
                    onClick = {
                        onGallery()
                        onDismiss()
                    }
                )
                OptionButton(
                    text = stringResource(R.string.select_images_dialog_option_files),
                    onClick = {
                        onFiles()
                        onDismiss()
                    }
                )
                OptionButton(
                    text = stringResource(R.string.select_images_dialog_option_directory),
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
                Text(stringResource(R.string.select_images_dialog_cancel))
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
