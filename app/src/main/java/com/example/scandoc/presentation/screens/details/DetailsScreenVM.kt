package com.example.scandoc.presentation.screens.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scandoc.domain.usecases.GetDocumentSetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsScreenVM @Inject constructor(
    private val getDocumentSetImagesUseCase: GetDocumentSetImagesUseCase,
) : ViewModel() {
    private val _photos = mutableStateOf<List<File>>(emptyList())
    val photos = _photos as State<List<File>>

    private val _text = mutableStateOf("")
    val text = _text as State<String>

    fun init(uuid: UUID) = viewModelScope.launch(Dispatchers.IO) {
        getDocumentSetImagesUseCase.execute(uuid)
            .let { _photos.value = it }

        _text.value = LoremIpsum(words = 300).values.first().toString()
    }

}
