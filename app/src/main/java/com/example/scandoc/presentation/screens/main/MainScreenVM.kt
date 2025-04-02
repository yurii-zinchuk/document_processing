package com.example.scandoc.presentation.screens.main

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.scandoc.domain.usecases.CreateDocumentSetUseCase
import com.example.scandoc.domain.usecases.DeleteDocumentSetUseCase
import com.example.scandoc.domain.usecases.GetAllDocumentSetsUseCase
import com.example.scandoc.presentation.mappers.MapperDocumentSetDomainToUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val mapperDocumentSetDomainToUI: MapperDocumentSetDomainToUI,
    private val createDocumentSetUseCase: CreateDocumentSetUseCase,
    private val deleteDocumentSetUseCase: DeleteDocumentSetUseCase,
    getAllDocumentSetsUseCase: GetAllDocumentSetsUseCase,
) : ViewModel() {
    private var selectedImagesUris = emptyList<Uri>()

    val documentSets = getAllDocumentSetsUseCase
        .execute()
        .map { pagingData ->
            pagingData.map { mapperDocumentSetDomainToUI.map(it) }
        }
        .cachedIn(viewModelScope)

    private val _enterSetNameDialogVisible = mutableStateOf(false)
    val enterSetNameDialogVisible = _enterSetNameDialogVisible as State<Boolean>

    private val _chooseImageSourceDialogVisible = mutableStateOf(false)
    val chooseImageSourceDialogVisible = _chooseImageSourceDialogVisible as State<Boolean>

    fun onDeleteDocumentSet(uuid: UUID) = viewModelScope.launch {
        deleteDocumentSetUseCase.execute(uuid)
    }

    fun onCreateDocumentSet(name: String) = viewModelScope.launch(Dispatchers.IO) {
        _enterSetNameDialogVisible.value = false
        createDocumentSetUseCase.execute(selectedImagesUris, name)
        selectedImagesUris = emptyList()
    }

    fun onAddDocumentSet() {
        _chooseImageSourceDialogVisible.value = true
    }

    fun onDismissChooseImageSourceDialog() {
        _chooseImageSourceDialogVisible.value = false
    }

    fun onImagesSelected(fileUris: List<Uri>) {
        selectedImagesUris = fileUris
        _enterSetNameDialogVisible.value = true
    }

    fun onImagesSelected(directoryUri: Uri) {
        selectedImagesUris = listOf(directoryUri)
        _enterSetNameDialogVisible.value = true
    }

    fun onDismissSetNameDialog() {
        _enterSetNameDialogVisible.value = false
    }
}
