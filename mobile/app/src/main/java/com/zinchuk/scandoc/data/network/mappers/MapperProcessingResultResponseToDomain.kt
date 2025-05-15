package com.zinchuk.scandoc.data.network.mappers

import com.zinchuk.scandoc.data.network.models.ProcessingResultResponse
import com.zinchuk.scandoc.domain.mappers.DataMapper
import com.zinchuk.scandoc.domain.models.ProcessedData
import javax.inject.Inject

class MapperProcessingResultResponseToDomain
    @Inject
    constructor() :
    DataMapper<ProcessingResultResponse, ProcessedData> {
        override fun map(data: ProcessingResultResponse): ProcessedData {
            return ProcessedData(
                data.text,
                data.entities,
            )
        }
    }
