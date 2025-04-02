package com.example.scandoc.data.network.api

import com.example.scandoc.data.network.models.TaskStatusResponse
import com.example.scandoc.data.network.models.UploadPDFResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProcessingApi {
    @Multipart
    @POST("upload/pdf")
    suspend fun uploadPdf(
        @Part file: MultipartBody.Part
    ): Response<UploadPDFResponse>

    @GET("tasks/{taskId}/status")
    suspend fun getTaskStatus(
        @Path("taskId") taskId: String
    ): Response<TaskStatusResponse>
}
