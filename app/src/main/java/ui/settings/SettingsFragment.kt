// ui/settings/SettingsFragment.kt
package ui.settings

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dadm.roreizq.QuotationShake.R
import dagger.hilt.android.AndroidEntryPoint
import data.settings.SettingsPreferenceDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var settingsPreferenceDataStore: SettingsPreferenceDataStore

    private lateinit var usernamePreference: EditTextPreference
    private lateinit var languagePreference: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Cargar las preferencias desde el archivo XML
        setPreferencesFromResource(R.xml.preferences_settings, rootKey)

        // Asignar el DataStore personalizado
        preferenceManager.preferenceDataStore = settingsPreferenceDataStore

        // Obtener la preferencia de nombre de usuario
        usernamePreference = findPreference<EditTextPreference>("user_name") ?: run {
            throw IllegalStateException("Preference 'user_name' not found in preferences XML")
        }

        // Obtener la preferencia de idioma
        languagePreference = findPreference<ListPreference>("language") ?: run {
            throw IllegalStateException("Preference 'language' not found in preferences XML")
        }

        // Actualizar los valores de las preferencias con los valores almacenados en el DataStore
        lifecycleScope.launch {
            loadUsername()
            loadLanguage()
        }
    }

    private suspend fun loadUsername() {
        // Obtener el nombre de usuario desde el DataStore
        val username = withContext(Dispatchers.IO) {
            settingsPreferenceDataStore.getString("user_name", "")
        }

        // Actualizar la UI con el nombre de usuario
        usernamePreference.text = username
    }

    private suspend fun loadLanguage() {
        // Obtener el idioma desde el DataStore
        val language = withContext(Dispatchers.IO) {
            settingsPreferenceDataStore.getString("language", "en") // "en" es el valor por defecto
        }

        // Actualizar la UI con el idioma
        languagePreference.value = language
    }

    override fun onResume() {
        super.onResume()

        // Actualizar los valores cada vez que el fragmento se vuelva visible
        lifecycleScope.launch {
            loadUsername()
            loadLanguage()
        }
    }
}