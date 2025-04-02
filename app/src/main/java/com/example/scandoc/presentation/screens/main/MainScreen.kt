package com.example.scandoc.presentation.screens.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scandoc.presentation.navigation.Route
import com.example.scandoc.presentation.screens.details.components.toolbar.MainToolbar
import com.example.scandoc.presentation.screens.main.components.ChooseImageSourceDialog
import com.example.scandoc.presentation.screens.main.components.DocumentSetItem
import com.example.scandoc.presentation.screens.main.components.SelectNameDialog

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    vm: MainScreenVM,
    navController: NavHostController,
) {
    val galleryImagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        uris
            .takeIf { it.isNotEmpty() }
            ?.let { vm.onImagesSelected(it) }
            ?: return@rememberLauncherForActivityResult
    }

    val filesImagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris
            .takeIf { it.isNotEmpty() }
            ?.let { vm.onImagesSelected(it) }
            ?: return@rememberLauncherForActivityResult
    }

    val directoryImagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri
            .takeIf { it != null }
            ?.let { vm.onImagesSelected(it) }
            ?: return@rememberLauncherForActivityResult
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val documentSets = vm.documentSets.collectAsLazyPagingItems()
        Column {
            MainToolbar()
            Spacer(modifier = Modifier.height(16.dp))
            if (documentSets.itemCount == 0) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null,
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = "Upload a PDF or multiple images to create your first document set.",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(50.dp),
                    onClick = vm::onAddDocumentSet
                ) {
                    Text("Create document set")
                }
                Spacer(modifier = Modifier.weight(1.5f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(
                        count = documentSets.itemCount,
                        key = { documentSets[it]?.uuid ?: 0 }
                    ) { idx ->
                        documentSets[idx]?.let {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItem()
                            ) {
                                DocumentSetItem(
                                    data = it,
                                    onClick = {
                                        navController.navigate(
                                            Route.DETAILS_SCREEN.route + "/${Uri.encode(it.uuid.toString())}"
                                        )
                                    },
                                    onDelete = vm::onDeleteDocumentSet
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                    // TODO: Test below cases
                    // Loading Indicator
                    if (documentSets.loadState.append is LoadState.Loading) {
                        item {
                            CircularProgressIndicator(
                                modifier = Modifier
                            )
                        }
                    }
                    // Error handling
                    if (documentSets.loadState.append is LoadState.Error) {
                        val error = (documentSets.loadState.append as LoadState.Error).error
                        item {
                            Text("Error loading: ${error.message}")
                        }
                    }
                }
            }
        }
        if (documentSets.itemCount != 0) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = 16.dp),
                onClick = vm::onAddDocumentSet
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    }
    if (vm.enterSetNameDialogVisible.value) {
        SelectNameDialog(
            onDismiss = vm::onDismissSetNameDialog,
            onConfirm = vm::onCreateDocumentSet,
        )
    }
    if (vm.chooseImageSourceDialogVisible.value) {
        ChooseImageSourceDialog(
            onDismiss = vm::onDismissChooseImageSourceDialog,
            onFiles = { filesImagePicker.launch(arrayOf("image/*")) },
            onGallery = { galleryImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onDirectory = { directoryImagePicker.launch(null) },
        )
    }
}
