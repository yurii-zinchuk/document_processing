package com.zinchuk.scandoc.presentation.screens.main.components.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.zinchuk.scandoc.R

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
                    text = stringResource(R.string.main_screen_title),
                    fontSize = 36.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        },
    )
}
