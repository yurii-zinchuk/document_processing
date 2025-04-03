package com.example.scandoc.data.network.mappers

import com.example.scandoc.domain.mappers.DataMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MapperFileToMultipart
    @Inject
    constructor() : DataMapper<File, MultipartBody.Part> {
        override fun map(data: File): MultipartBody.Part {
            val requestBody = data.asRequestBody(PDF_MIME_TYPE.toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(FIELD_NAME, data.name, requestBody)
        }

        private companion object {
            private const val PDF_MIME_TYPE = "application/pdf"
            private const val FIELD_NAME = "pdf_file"
        }
    }
