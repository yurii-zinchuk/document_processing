package com.example.scandoc.presentation.screens.main

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.work.WorkInfo
import com.example.scandoc.domain.models.ProcessingStatus
import com.example.scandoc.domain.usecases.CreateDocumentSetUseCase
import com.example.scandoc.domain.usecases.DeleteDocumentSetUseCase
import com.example.scandoc.domain.usecases.GetAllDocumentSetsUseCase
import com.example.scandoc.domain.usecases.GetDocumentSetWorkInfoUseCase
import com.example.scandoc.domain.usecases.UpdateProcessingStatusUseCase
import com.example.scandoc.presentation.mappers.MapperDocumentSetDomainToUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainScreenVM @Inject constructor(
    private val mapperDocumentSetDomainToUI: MapperDocumentSetDomainToUI,
    private val createDocumentSetUseCase: CreateDocumentSetUseCase,
    private val deleteDocumentSetUseCase: DeleteDocumentSetUseCase,
    private val getDocumentSetWorkInfoUseCase: GetDocumentSetWorkInfoUseCase,
    private val updateProcessingStatusUseCase: UpdateProcessingStatusUseCase,
    getAllDocumentSetsUseCase: GetAllDocumentSetsUseCase,
) : ViewModel() {
    private var selectedImagesUris = emptyList<Uri>()

    val documentSets = getAllDocumentSetsUseCase
        .execute()
        .map { pagingData ->
            pagingData.map {
//                handleProcessingStatus(it.uuid)
                mapperDocumentSetDomainToUI.map(it)
            }
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

    private fun handleProcessingStatus(uuid: UUID) {
        getDocumentSetWorkInfoUseCase.execute(uuid)
            .mapNotNull { infos -> infos.firstOrNull() }
            .take(1)
            .onEach {
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> ProcessingStatus.RUNNING
                    WorkInfo.State.RUNNING -> ProcessingStatus.RUNNING
                    WorkInfo.State.SUCCEEDED -> ProcessingStatus.SUCCESS
                    WorkInfo.State.FAILED -> ProcessingStatus.FAILURE
                    WorkInfo.State.BLOCKED -> ProcessingStatus.RUNNING
                    WorkInfo.State.CANCELLED -> ProcessingStatus.SUCCESS
                }.let { status -> updateProcessingStatusUseCase.execute(uuid, status) }
            }
            .launchIn(viewModelScope)
    }
}
