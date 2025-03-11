// di/SettingsModule.kt
package di

import data.settings.SettingsDataSource
import data.settings.SettingsDataSourceImpl
import data.settings.SettingsRepository
import data.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsBinderModule {

    @Binds
    @Singleton
    abstract fun bindSettingsDataSource(impl: SettingsDataSourceImpl): SettingsDataSource

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}