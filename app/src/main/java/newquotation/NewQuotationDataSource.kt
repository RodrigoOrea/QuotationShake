// data/newquotation/NewQuotationDataSource.kt
package data.newquotation

import RemoteQuotationDto
import retrofit2.Response

interface NewQuotationDataSource {
    suspend fun getQuotation(lang: String): Response<RemoteQuotationDto>
}