package com.example.scandoc.presentation.screens.details.components.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
@ExperimentalMaterial3Api
fun MainToolbar() {
    TopAppBar(
        modifier =
        Modifier
            .fillMaxWidth(),
        title = {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Document Sets",
                    fontSize = 36.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        },
    )
}
