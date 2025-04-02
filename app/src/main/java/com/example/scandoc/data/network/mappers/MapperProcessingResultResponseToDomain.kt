package com.example.scandoc.data.network.mappers

import com.example.scandoc.data.network.models.ProcessingResultResponse
import com.example.scandoc.domain.mappers.DataMapper
import com.example.scandoc.domain.models.ProcessedData
import javax.inject.Inject

class MapperProcessingResultResponseToDomain @Inject constructor() :
    DataMapper<ProcessingResultResponse, ProcessedData> {

    override fun map(data: ProcessingResultResponse): ProcessedData {
        return ProcessedData(
            data.text,
            data.entities,
        )
    }

}
