package com.zinchuk.scandoc.data.network.models

import com.google.gson.annotations.SerializedName

data class CreateTaskResponse(
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("upload_url")
    val uploadURL: String,
)
