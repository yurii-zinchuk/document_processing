package com.example.scandoc.presentation.screens.details.components.toolbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
@ExperimentalMaterial3Api
fun Toolbar(
    navController: NavHostController,
) {
    TopAppBar(
        modifier =
        Modifier
            .fillMaxWidth(),
        title = {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Set Name",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            ) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Outlined.Refresh, null)
            }
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Outlined.Edit, null)
            }
        }
    )
}
