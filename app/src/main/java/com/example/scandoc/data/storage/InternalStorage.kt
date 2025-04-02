package com.example.scandoc.data.storage

import java.io.File
import java.util.UUID

interface InternalStorage {

    fun getDirectory(uuid: UUID): File

}
