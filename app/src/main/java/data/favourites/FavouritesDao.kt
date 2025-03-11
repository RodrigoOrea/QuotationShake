package data.favourites

import androidx.room.*
import com.tuapp.data.favourites.model.DatabaseQuotationDto
import domain.model.Quotation
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotation(quotation: DatabaseQuotationDto)

    @Delete
    suspend fun deleteQuotation(quotation: DatabaseQuotationDto)

    @Query("SELECT * FROM ${FavouritesContract.QuotationTable.TABLE_NAME}")
    fun getAllQuotations(): Flow<List<DatabaseQuotationDto>>

    @Query("SELECT * FROM ${FavouritesContract.QuotationTable.TABLE_NAME} WHERE ${FavouritesContract.QuotationTable.COLUMN_ID} = :id")
    fun getQuotationById(id: String): Flow<DatabaseQuotationDto?>

    @Query("DELETE FROM ${FavouritesContract.QuotationTable.TABLE_NAME}")
    suspend fun deleteAllQuotations()

    @Query("SELECT * FROM favourite_quotations WHERE id = :id")
    fun getFavouriteQuotationById(id: String): Flow<Quotation?>
}
