package com.zinchuk.scandoc.data.network.models

import com.google.gson.annotations.SerializedName

data class ProcessingResultResponse(
    @SerializedName("text")
    val text: String,
    @SerializedName("entities")
    val entities: Map<String, List<String>>,
)
