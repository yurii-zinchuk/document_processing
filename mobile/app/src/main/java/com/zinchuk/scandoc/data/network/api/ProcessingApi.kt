package com.zinchuk.scandoc.data.network.api

import com.zinchuk.scandoc.data.network.models.CreateTaskResponse
import com.zinchuk.scandoc.data.network.models.TaskStatusResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Url

interface ProcessingApi {
    @GET("/tasks/create")
    suspend fun getTaskInfo(): Response<CreateTaskResponse>

    @PUT
    suspend fun uploadZIPFile(
        @Url presignedUrl: String,
        @Body zipFile: RequestBody,
    ): Response<Unit>

    @GET("/tasks/{taskId}/status")
    suspend fun getTaskStatus(
        @Path("taskId") taskId: String,
    ): Response<TaskStatusResponse>
}
