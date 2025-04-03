package com.example.scandoc.data.network.models

import com.google.gson.annotations.SerializedName

data class ProcessingResultResponse(
    @SerializedName("text")
    val text: String,
    @SerializedName("entities")
    val entities: List<String>,
)
