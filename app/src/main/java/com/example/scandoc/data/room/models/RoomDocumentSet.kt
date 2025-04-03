package com.example.scandoc.data.room.models

import androidx.room.Entity

@Entity(primaryKeys = ["uuid"], tableName = "document_set")
data class RoomDocumentSet(
    val uuid: String,
    val name: String,
    val timestamp: Long,
)
