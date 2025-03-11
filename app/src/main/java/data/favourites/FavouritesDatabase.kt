package data.favourites

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tuapp.data.favourites.model.DatabaseQuotationDto

@Database(entities = [DatabaseQuotationDto::class], version = 1, exportSchema = false)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun favouritesDao(): FavouritesDao
}
