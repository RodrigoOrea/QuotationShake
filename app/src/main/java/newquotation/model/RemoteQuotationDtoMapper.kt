package com.tuapp.newquotation.model

import RemoteQuotationDto
import domain.model.Quotation
import retrofit2.Response
import java.io.IOException

// data/newquotation/model/RemoteQuotationDtoMapper.kt
fun RemoteQuotationDto.toDomain() = Quotation(
    id = quoteLink,
    text = quoteText,
    author = quoteAuthor.ifEmpty { "Anonymous" }
)

fun Response<RemoteQuotationDto>.toDomain(): Result<Quotation> {
    return if (isSuccessful) {
        body()?.let { dto ->
            Result.success(dto.toDomain())
        } ?: Result.failure(IOException("Empty response body"))
    } else {
        Result.failure(IOException("API Error: ${code()}"))
    }
}