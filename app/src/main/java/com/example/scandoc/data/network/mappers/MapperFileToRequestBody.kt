package com.example.scandoc.data.network.mappers

import com.example.scandoc.domain.mappers.DataMapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MapperFileToRequestBody
    @Inject
    constructor() : DataMapper<File, RequestBody> {
        override fun map(data: File): RequestBody {
            return data.asRequestBody(ZIP_MIME_TYPE.toMediaTypeOrNull())
        }

        private companion object {
            private const val ZIP_MIME_TYPE = "application/zip"
        }
    }
