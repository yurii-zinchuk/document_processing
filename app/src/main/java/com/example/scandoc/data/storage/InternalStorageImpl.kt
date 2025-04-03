package com.example.scandoc.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class InternalStorageImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : InternalStorage {
        override fun getDirectory(uuid: UUID): File = File(context.filesDir, uuid.toString())
    }
