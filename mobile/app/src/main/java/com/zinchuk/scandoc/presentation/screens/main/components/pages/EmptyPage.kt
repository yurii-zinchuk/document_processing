package com.zinchuk.scandoc.presentation.screens.main.components.pages

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zinchuk.scandoc.R

@Composable
fun ColumnScope.EmptyPage(onCreateDocumentSet: () -> Unit) {
    Spacer(modifier = Modifier.weight(1f))
    Icon(
        modifier =
            Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
        imageVector = Icons.Filled.AddCircle,
        contentDescription = null,
        tint = Color.LightGray,
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        modifier =
            Modifier
                .align(Alignment.CenterHorizontally),
        text = stringResource(R.string.main_screen_create_document_set_prompt),
        textAlign = TextAlign.Center,
        fontSize = 22.sp,
        color = Color.Gray,
    )
    Spacer(modifier = Modifier.height(48.dp))
    Button(
        modifier =
            Modifier
                .align(Alignment.CenterHorizontally)
                .height(50.dp),
        onClick = onCreateDocumentSet,
    ) {
        Text(stringResource(R.string.main_screen_create_document_set_button))
    }
    Spacer(modifier = Modifier.weight(1.5f))
}
