package com.example.scandoc.presentation.screens.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scandoc.domain.usecases.GetDocumentSetImagesUseCase
import com.example.scandoc.domain.usecases.GetProcessedDataUseCase
import com.example.scandoc.domain.usecases.ProcessDocumentSetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsScreenVM @Inject constructor(
    private val getDocumentSetImagesUseCase: GetDocumentSetImagesUseCase,
    private val processDocumentSetUseCase: ProcessDocumentSetUseCase,
    private val getProcessedDataUseCase: GetProcessedDataUseCase,
) : ViewModel() {
    private var documentSetUUID: UUID? = null
    private var processingJob: Job? = null

    // State private
    private val _isProcessing = mutableStateOf(false)
    private val _photos = mutableStateOf<List<File>>(emptyList())
    private val _text = mutableStateOf<String?>(null)
    private val _entities = mutableStateOf<List<String>?>(null)

    // State public
    val isProcessing = _isProcessing as State<Boolean>
    val photos = _photos as State<List<File>>
    val text = _text as State<String?>
    val entities = _entities as State<List<String>?>

    fun init(uuid: UUID) = viewModelScope.launch(Dispatchers.IO) {
        documentSetUUID = uuid

        getDocumentSetImagesUseCase.execute(uuid)
            .let { _photos.value = it }
        getProcessedDataUseCase.execute(uuid)
            .let {
                _text.value = it.text
                _entities.value = it.entities
            }
    }

    fun onProcessDocumentSet() = viewModelScope.launch(Dispatchers.IO) {
        documentSetUUID?.let {
            _isProcessing.value = true
            try {
                processDocumentSetUseCase.execute(it)
            } catch (_: Exception) {
                null
            }
        }?.let {
            _isProcessing.value = false
            _text.value = it.text ?: return@let
            _entities.value = it.entities ?: return@let
        }
    }.also { processingJob = it }

    fun onStopProcessingDocumentSet() {
        processingJob?.cancel()
        _isProcessing.value = false
        // TODO: stop processing by job ID
    }

}
