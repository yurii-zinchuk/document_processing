package com.example.scandoc.domain.mappers

interface DataMapper<IN, OUT> {
    fun map(data: IN): OUT
}
