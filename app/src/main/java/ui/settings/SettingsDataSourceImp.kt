package data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsDataSource {

    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val LANGUAGE = stringPreferencesKey("language")
        private const val DEFAULT_LANGUAGE = "en"
    }

    override fun getUserName(): Flow<String> = getString(USER_NAME)

    override suspend fun getUserNameSnapshot(): String = getStringSnapshot(USER_NAME)

    override suspend fun setUserName(userName: String) = setString(USER_NAME, userName)

    override fun getLanguage(): Flow<String> = getString(LANGUAGE, DEFAULT_LANGUAGE)

    override suspend fun getLanguageSnapshot(): String = getStringSnapshot(LANGUAGE, DEFAULT_LANGUAGE)

    override suspend fun setLanguage(language: String) = setString(LANGUAGE, language)

    private fun getString(key: Preferences.Key<String>, defaultValue: String = ""): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    private suspend fun getStringSnapshot(key: Preferences.Key<String>, defaultValue: String = ""): String {
        return dataStore.data.first()[key] ?: defaultValue
    }

    private suspend fun setString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
