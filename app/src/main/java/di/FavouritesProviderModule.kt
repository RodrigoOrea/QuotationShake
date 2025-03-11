package di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.favourites.FavouritesContract
import data.favourites.FavouritesDao
import data.favourites.FavouritesDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavouritesProviderModule {

    @Provides
    @Singleton
    fun provideFavouritesDatabase(@ApplicationContext context: Context): FavouritesDatabase {
        return Room.databaseBuilder(
            context,
            FavouritesDatabase::class.java,
            FavouritesContract.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideFavouritesDao(database: FavouritesDatabase): FavouritesDao {
        return database.favouritesDao()
    }
}
