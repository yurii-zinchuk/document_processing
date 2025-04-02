package com.example.scandoc.data.network.models

import com.google.gson.annotations.SerializedName

data class TaskStatusResponse(
    @SerializedName("status")
    val status: TaskStatus,
    @SerializedName("result")
    val result: ProcessingResultResponse? = null
) {
    enum class TaskStatus {
        @SerializedName("done")
        DONE,
        @SerializedName("error")
        ERROR,
        @SerializedName("processing")
        PROCESSING;
    }
}
