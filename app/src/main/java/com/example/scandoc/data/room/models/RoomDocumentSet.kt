package com.example.scandoc.data.room.models

import androidx.room.Entity
import com.example.scandoc.domain.models.ProcessingStatus

@Entity(primaryKeys = ["uuid"], tableName = "document_set")
data class RoomDocumentSet(
    val uuid: String,
    val name: String,
    val timestamp: Long,
    val processingStatus: ProcessingStatus,
)
