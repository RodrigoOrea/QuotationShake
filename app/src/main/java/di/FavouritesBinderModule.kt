package di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.favourites.FavouritesDataSource
import data.favourites.FavouritesDataSourceImpl
import data.favourites.FavouritesRepository
import data.favourites.FavouritesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavouritesBinderModule {

    @Binds
    @Singleton
    abstract fun bindFavouritesDataSource(
        favouritesDataSourceImpl: FavouritesDataSourceImpl
    ): FavouritesDataSource

    @Binds
    @Singleton
    abstract fun bindFavouritesRepository(
        favouritesRepositoryImpl: FavouritesRepositoryImpl
    ): FavouritesRepository
}
