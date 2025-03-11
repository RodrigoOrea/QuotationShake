package data.favourites

import data.favourites.model.toDatabaseDto
import data.favourites.model.toDomain
import domain.model.Quotation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(
    private val dataSource: FavouritesDataSource
) : FavouritesRepository {

    override suspend fun insertQuotation(quotation: Quotation) {
        dataSource.insertQuotation(quotation.toDatabaseDto())
    }

    override suspend fun deleteQuotation(quotation: Quotation) {
        dataSource.deleteQuotation(quotation.toDatabaseDto())
    }

    override fun getAllQuotations(): Flow<List<Quotation>> {
        return dataSource.getAllQuotations().map { dtoList ->
            dtoList.map { dto -> dto.toDomain() }
        }
    }

    override fun getQuotationById(id: String): Flow<Quotation?> {
        return dataSource.getQuotationById(id).map { dto ->
            dto?.toDomain()
        }
    }

    override suspend fun deleteAllQuotations() {
        dataSource.deleteAllQuotations()
    }

    override suspend fun getQuotationByPosition(position: Int): Quotation? {
        val quotations = getAllQuotations().first()
        return quotations.getOrNull(position)
    }
}
