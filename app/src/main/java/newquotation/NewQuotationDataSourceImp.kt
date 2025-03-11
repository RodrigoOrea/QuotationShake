// data/newquotation/NewQuotationDataSourceImpl.kt
package data.newquotation

import RemoteQuotationDto
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

interface NewQuotationRetrofit {
    @GET("api/1.0/?method=getQuote&format=json&lang=en")
    suspend fun getQuotation(@Query("lang") lang: String): Response<RemoteQuotationDto>
}

class NewQuotationDataSourceImpl @Inject constructor(
    private val retrofit: Retrofit
) : NewQuotationDataSource {

    private val retrofitQuotationService = retrofit.create(NewQuotationRetrofit::class.java)

    override suspend fun getQuotation(lang: String): Response<RemoteQuotationDto> {
        return try {
            retrofitQuotationService.getQuotation(lang)
        } catch (e: Exception) {
            Response.error(
                400, // Código de error
                ResponseBody.create(
                    MediaType.parse("text/plain"), // Usar MediaType.parse
                    e.toString()
                )
            )
        }
    }
}