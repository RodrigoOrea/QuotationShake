package data.settings

import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsPreferenceDataStore @Inject constructor(
    private val repository: SettingsRepository
) : PreferenceDataStore() {

    override fun putString(key: String, value: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            when (key) {
                "user_name" -> repository.setUserName(value ?: "")
                "language" -> repository.setLanguage(value ?: "en")
            }
        }
    }

    override fun getString(key: String, defValue: String?): String? {
        return runBlocking(Dispatchers.IO) {
            when (key) {
                "user_name" -> repository.getUserNameSnapshot()
                "language" -> repository.getLanguageSnapshot()
                else -> defValue
            }
        }
    }
}
