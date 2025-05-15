package com.zinchuk.scandoc.domain.mappers

interface DataMapper<IN, OUT> {
    fun map(data: IN): OUT
}
