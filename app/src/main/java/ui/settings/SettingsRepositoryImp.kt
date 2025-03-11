package data.settings

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: SettingsDataSource
) : SettingsRepository {

    override fun getUserName(): Flow<String> = dataSource.getUserName()

    override suspend fun getUserNameSnapshot(): String = dataSource.getUserNameSnapshot()

    override suspend fun setUserName(userName: String) = dataSource.setUserName(userName)

    override fun getLanguage(): Flow<String> = dataSource.getLanguage()

    override suspend fun getLanguageSnapshot(): String = dataSource.getLanguageSnapshot()

    override suspend fun setLanguage(language: String) = dataSource.setLanguage(language)
}
