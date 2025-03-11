// data/newquotation/NewQuotationRepository.kt
package data.newquotation

import domain.model.Quotation
interface NewQuotationRepository {
    suspend fun getNewQuotation(): Result<Quotation>
}