package data.favourites

import com.tuapp.data.favourites.model.DatabaseQuotationDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesDataSourceImpl @Inject constructor(
    private val favouritesDao: FavouritesDao
) : FavouritesDataSource {

    override suspend fun insertQuotation(quotation: DatabaseQuotationDto) {
        favouritesDao.insertQuotation(quotation)
    }

    override suspend fun deleteQuotation(quotation: DatabaseQuotationDto) {
        favouritesDao.deleteQuotation(quotation)
    }

    override fun getAllQuotations(): Flow<List<DatabaseQuotationDto>> {
        return favouritesDao.getAllQuotations()
    }

    override fun getQuotationById(id: String): Flow<DatabaseQuotationDto?> {
        return favouritesDao.getQuotationById(id)
    }

    override suspend fun deleteAllQuotations() {
        favouritesDao.deleteAllQuotations()
    }
}
