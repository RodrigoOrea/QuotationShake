import com.squareup.moshi.JsonClass

// data/newquotation/model/RemoteQuotationDto.kt
@JsonClass(generateAdapter = true)
data class RemoteQuotationDto(
    val quoteText: String,
    val quoteAuthor: String,
    val senderName: String,
    val senderLink: String,
    val quoteLink: String
)