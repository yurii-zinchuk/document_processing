package com.zinchuk.scandoc.data.repositories

import com.zinchuk.scandoc.data.storage.InternalStorage
import com.zinchuk.scandoc.domain.repositories.ZIPRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ZIPRepositoryImpl
    @Inject
    constructor(
        private val internalStorage: InternalStorage,
    ) : ZIPRepository {
        override suspend fun createZIP(
            uuid: UUID,
            imageFiles: List<File>,
        ) {
            val destinationDirectory = getInternalZIPDirectory(uuid)
            if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

            val zipDestinationFile =
                File(
                    destinationDirectory,
                    ZIP_FILE_NAME,
                )

            withContext(Dispatchers.IO) {
                ZipOutputStream(BufferedOutputStream(FileOutputStream(zipDestinationFile))).use { zipOut ->
                    imageFiles.forEach { file ->
                        val entry = ZipEntry(file.name)
                        zipOut.putNextEntry(entry)
                        file.inputStream().use { input ->
                            input.copyTo(zipOut)
                        }
                        zipOut.closeEntry()
                    }
                }
            }
        }

        override suspend fun getZIP(uuid: UUID): File {
            return File(
                getInternalZIPDirectory(uuid),
                ZIP_FILE_NAME,
            )
        }

        private fun getInternalZIPDirectory(uuid: UUID): File = File(internalStorage.getDirectory(uuid), ZIP_DIRECTORY)

        private companion object {
            private const val ZIP_FILE_NAME = "data.zip"
            private const val ZIP_DIRECTORY = "zip"
        }
    }
