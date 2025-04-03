package com.example.scandoc.data.repositories

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.scandoc.data.storage.InternalStorage
import com.example.scandoc.domain.repositories.ImagesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImagesRepositoryImpl
    @Inject
    constructor(
        private val internalStorage: InternalStorage,
        @ApplicationContext private val context: Context,
    ) : ImagesRepository {
        override suspend fun saveImages(
            sourceURIs: List<Uri>,
            uuid: UUID,
        ): Boolean {
            val imageURIs =
                getFinalImageURIs(
                    getSourceImageDirectory(sourceURIs),
                    sourceURIs,
                ).ifEmpty { return@saveImages false }

            val destinationDirectory = getInternalImagesDirectory(uuid)
            if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

            imageURIs.forEachIndexed { idx, imageURI ->
                val destinationFile =
                    File(
                        destinationDirectory,
                        "${idx.inc()}.$IMAGE_EXT",
                    )
                context
                    .contentResolver
                    .openInputStream(imageURI)
                    .use { input ->
                        FileOutputStream(destinationFile)
                            .use { output -> input?.copyTo(output) }
                    }
            }

            return true
        }

        override suspend fun getImages(uuid: UUID): List<File> {
            val directory = getInternalImagesDirectory(uuid)
            if (!directory.exists() || !directory.isDirectory) return emptyList()

            return directory
                .listFiles { file -> file.isFile && file.canRead() }
                ?.toList()
                ?.sortedBy { file ->
                    file
                        .name
                        .substringBefore(IMAGE_EXT.first())
                        .toIntOrNull()
                }
                ?: emptyList()
        }

        override suspend fun deleteImages(uuid: UUID) {
            getInternalImagesDirectory(uuid)
                .takeIf { it.exists() && it.isDirectory }
                ?.deleteRecursively()
        }

        private fun getImageUrisFromDirectory(directory: DocumentFile): List<Uri> {
            val result = mutableListOf<Uri>()

            directory
                .listFiles()
                .forEach { file ->
                    if (file.isDirectory && !file.isHidden()) {
                        result += getImageUrisFromDirectory(file)
                    } else if (file.isFile && file.isImage()) {
                        result += file.uri
                    }
                }

            return result
        }

        private fun getSourceImageDirectory(sourceURIs: List<Uri>): DocumentFile? =
            try {
                DocumentFile.fromTreeUri(context, sourceURIs.first())
            } catch (_: Exception) {
                null
            }

        private fun getFinalImageURIs(
            sourceImageDirectory: DocumentFile?,
            sourceURIs: List<Uri>,
        ): List<Uri> =
            if (sourceImageDirectory?.isDirectory == true) {
                getImageUrisFromDirectory(sourceImageDirectory)
            } else {
                sourceURIs
            }

        private fun getInternalImagesDirectory(uuid: UUID): File = File(internalStorage.getDirectory(uuid), IMAGES_DIRECTORY)

        private fun DocumentFile.isHidden(): Boolean = name?.startsWith(HIDDEN_FILE_SYMBOL) == true

        private fun DocumentFile.isImage(): Boolean = type?.startsWith(IMAGE_MIME_TYPE) == true

        private companion object {
            private const val IMAGES_DIRECTORY = "img"
            private const val IMAGE_EXT = ".png"
            private const val IMAGE_MIME_TYPE = "image/"
            private const val HIDDEN_FILE_SYMBOL = "."
        }
    }
