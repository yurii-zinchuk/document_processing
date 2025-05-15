package com.zinchuk.scandoc.presentation.screens.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zinchuk.scandoc.presentation.screens.main.components.dialogs.ChooseImageSourceDialog
import com.zinchuk.scandoc.presentation.screens.main.components.dialogs.SetNameDialog
import com.zinchuk.scandoc.presentation.screens.main.components.pages.DocumentSetsPage
import com.zinchuk.scandoc.presentation.screens.main.components.pages.EmptyPage
import com.zinchuk.scandoc.presentation.screens.main.components.toolbar.MainToolbar

private const val IMAGE_MIME_TYPE = "image/*"

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(
    vm: MainScreenVM,
    navController: NavHostController,
) {
    val galleryImagePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(),
        ) { uris ->
            uris
                .takeIf { it.isNotEmpty() }
                ?.let { vm.onImagesSelected(it) }
                ?: return@rememberLauncherForActivityResult
        }
    val filesImagePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenMultipleDocuments(),
        ) { uris ->
            uris
                .takeIf { it.isNotEmpty() }
                ?.let { vm.onImagesSelected(it) }
                ?: return@rememberLauncherForActivityResult
        }
    val directoryImagePicker =
        rememberLauncherForActivityResult(
            ActivityResultContracts.OpenDocumentTree(),
        ) { uri ->
            uri
                .takeIf { it != null }
                ?.let { vm.onImagesSelected(it) }
                ?: return@rememberLauncherForActivityResult
        }

    Box(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        val documentSets = vm.documentSets.collectAsLazyPagingItems()
        Column {
            MainToolbar()
            Spacer(modifier = Modifier.height(16.dp))
            if (documentSets.itemCount == 0) {
                EmptyPage(vm::onAddDocumentSet)
            } else {
                DocumentSetsPage(
                    documentSets = documentSets,
                    navController = navController,
                    onDeleteDocumentSet = vm::onDeleteDocumentSet,
                )
            }
        }
        if (documentSets.itemCount != 0) {
            FloatingActionButton(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding()
                        .padding(end = 16.dp),
                onClick = vm::onAddDocumentSet,
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    }
    if (vm.enterSetNameDialogVisible.value) {
        SetNameDialog(
            onDismiss = vm::onDismissSetNameDialog,
            onConfirm = vm::onCreateDocumentSet,
        )
    }
    if (vm.chooseImageSourceDialogVisible.value) {
        ChooseImageSourceDialog(
            onDismiss = vm::onDismissChooseImageSourceDialog,
            onFiles = { filesImagePicker.launch(arrayOf(IMAGE_MIME_TYPE)) },
            onDirectory = { directoryImagePicker.launch(null) },
            onGallery = { galleryImagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
        )
    }
}
