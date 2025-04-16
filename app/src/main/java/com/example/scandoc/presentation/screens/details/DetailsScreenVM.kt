package com.example.scandoc.presentation.screens.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scandoc.domain.usecases.CancelProcessingWorkUseCase
import com.example.scandoc.domain.usecases.GetDocumentSetByUUIDUseCase
import com.example.scandoc.domain.usecases.GetDocumentSetImagesUseCase
import com.example.scandoc.domain.usecases.GetDocumentSetWorkInfoUseCase
import com.example.scandoc.domain.usecases.GetProcessedDataUseCase
import com.example.scandoc.domain.usecases.ProcessDocumentSetUseCase
import com.example.scandoc.utils.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsScreenVM
    @Inject
    constructor(
        private val getDocumentSetImagesUseCase: GetDocumentSetImagesUseCase,
        private val processDocumentSetUseCase: ProcessDocumentSetUseCase,
        private val getProcessedDataUseCase: GetProcessedDataUseCase,
        private val getDocumentSetWorkInfoUseCase: GetDocumentSetWorkInfoUseCase,
        private val cancelProcessingWorkUseCase: CancelProcessingWorkUseCase,
        private val getDocumentSetByUUIDUseCase: GetDocumentSetByUUIDUseCase,
    ) : ViewModel() {
        private lateinit var documentSetUUID: UUID

        // State private
        private val _isProcessing = mutableStateOf(false)
        private val _photos = mutableStateOf<List<File>>(emptyList())
        private val _text = mutableStateOf<String?>(null)
        private val _entities = mutableStateOf<Map<String, List<String>>?>(null)
        private val _title = mutableStateOf(EMPTY_STRING)

        // State public
        val isProcessing = _isProcessing as State<Boolean>
        val photos = _photos as State<List<File>>
        val text = _text as State<String?>
        val entities = _entities as State<Map<String, List<String>>?>
        val title = _title as State<String>

        fun init(uuid: UUID) =
            viewModelScope.launch(Dispatchers.IO) {
                documentSetUUID = uuid

                observeProcessingWork(uuid)
                initDocumentSetData()
            }

        fun onProcessDocumentSet() =
            viewModelScope.launch(Dispatchers.IO) {
                documentSetUUID.let {
                    _isProcessing.value = true
                    try {
                        processDocumentSetUseCase.execute(it)
                    } catch (_: Exception) {
                        // no-op
                    }
                    observeProcessingWork(documentSetUUID)
                }
            }

        fun onStopProcessingDocumentSet() =
            viewModelScope.launch(Dispatchers.IO) {
                documentSetUUID
                    .let { cancelProcessingWorkUseCase.execute(it) }
                    .let { _isProcessing.value = false }
            }

        private fun observeProcessingWork(uuid: UUID) {
            getDocumentSetWorkInfoUseCase.execute(uuid)
                .mapNotNull { infos -> infos.firstOrNull() }
                .onEach {
                    if (it.state.isFinished) {
                        onProcessingFinished(uuid)
                    } else {
                        onProcessingRunning()
                    }
                }
                .launchIn(viewModelScope)
        }

        private suspend fun initDocumentSetData() {
            getDocumentSetByUUIDUseCase.execute(documentSetUUID)
                .let { _title.value = it.name }
            getDocumentSetImagesUseCase.execute(documentSetUUID)
                .let { _photos.value = it }
        }

        private fun onProcessingFinished(uuid: UUID) {
            _isProcessing.value = false
            getProcessedDataUseCase.execute(uuid)
                .let {
                    _text.value = it.text
                    _entities.value = it.entities
                }
        }

        private fun onProcessingRunning() {
            _isProcessing.value = true
        }
    }
