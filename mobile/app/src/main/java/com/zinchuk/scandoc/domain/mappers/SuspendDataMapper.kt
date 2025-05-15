package com.zinchuk.scandoc.domain.mappers

interface SuspendDataMapper<IN, OUT> {
    suspend fun map(data: IN): OUT
}
