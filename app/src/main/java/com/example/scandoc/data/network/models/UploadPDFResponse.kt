package com.example.scandoc.data.network.models

import com.google.gson.annotations.SerializedName

data class UploadPDFResponse(
    @SerializedName("taskId")
    val taskId: String,
)
