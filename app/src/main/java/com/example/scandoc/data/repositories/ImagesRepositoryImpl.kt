package com.example.scandoc.data.repositories

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.scandoc.domain.repositories.ImagesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImagesRepository {
    override suspend fun saveImages(sourceURIs: List<Uri>, uuid: UUID): Boolean {
        val imageURIs = getFinalImageURIs(
            getSourceImageDirectory(sourceURIs),
            sourceURIs,
        ).ifEmpty { return@saveImages false }

        val destinationDirectory = getInternalImagesDirectory(uuid)
        if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

        imageURIs.forEachIndexed { idx, imageURI ->
            val destinationFile = File(
                destinationDirectory,
                FILE_NAME_TEMPLATE.format(idx.inc()) + IMAGE_EXT
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
                    .split(FILE_NAME_SEPARATOR)
                    .lastOrNull()
                    ?.substringBefore(IMAGE_EXT.first())
                    ?.toIntOrNull()
            }
            ?: emptyList()
    }

    override suspend fun deleteImages(uuid: UUID) {
        val directory = getInternalImagesDirectory(uuid)
        if (!directory.exists() || !directory.isDirectory) {
            return
        }

        directory.deleteRecursively()
    }

    override suspend fun createPDF(uuid: UUID) {
        val imageFiles = getImages(uuid)
        val pdfFile = PdfDocument()

        imageFiles.forEachIndexed { idx, image ->
            val bitmap = BitmapFactory.decodeFile(image.absolutePath)
            val pageInfo = PdfDocument
                .PageInfo
                .Builder(
                    bitmap.width,
                    bitmap.height,
                    idx.inc(),
                ).create()

            val page = pdfFile.startPage(pageInfo)
            val canvas = page.canvas

            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfFile.finishPage(page)

            val destinationDirectory = File(context.filesDir, uuid.toString())
            val pdfDestinationFile = File(
                destinationDirectory,
                PDF_FILE_NAME,
            )

            if (!destinationDirectory.exists()) destinationDirectory.mkdirs()

            FileOutputStream(pdfDestinationFile).use { out ->
                pdfFile.writeTo(out)
            }
        }
        pdfFile.close()
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
        sourceURIs: List<Uri>
    ): List<Uri> =
        if (sourceImageDirectory?.isDirectory == true) {
            getImageUrisFromDirectory(sourceImageDirectory)
        } else {
            sourceURIs
        }

    private fun getInternalImagesDirectory(uuid: UUID): File =
        File(context.filesDir, uuid.toString())

    private fun DocumentFile.isHidden(): Boolean =
        name?.startsWith(HIDDEN_FILE_SYMBOL) == true

    private fun DocumentFile.isImage(): Boolean =
        type?.startsWith(IMAGE_MIME_TYPE) == true

    private companion object {
        private const val FILE_NAME_TEMPLATE = "IMAGE_FILE_%s"
        private const val PDF_FILE_NAME = "PDF_PHOTOS.pdf"
        private const val IMAGE_EXT = ".png"
        private const val IMAGE_MIME_TYPE = "image/"
        private const val FILE_NAME_SEPARATOR = "_"
        private const val HIDDEN_FILE_SYMBOL = "."
    }
}
