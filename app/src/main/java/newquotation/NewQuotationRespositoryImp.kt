package data.newquotation

import com.tuapp.newquotation.model.toDomain
import data.settings.SettingsRepository
import domain.model.Quotation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import utils.NoInternetException
import javax.inject.Inject

class NewQuotationRepositoryImpl @Inject constructor(
    private val dataSource: NewQuotationDataSource,
    private val connectivityChecker: ConnectivityChecker,
    private val settingsRepository: SettingsRepository
) : NewQuotationRepository {

    private lateinit var language: String

    init {
        CoroutineScope(SupervisorJob()).launch {
            settingsRepository.getLanguage().collect { languageCode ->
                language = languageCode.ifEmpty { "en" }
            }
        }
    }

    override suspend fun getNewQuotation(): Result<Quotation> {
        return if (connectivityChecker.isConnectionAvailable()) {
            dataSource.getQuotation(language).toDomain() // Usar el idioma almacenado
        } else {
            Result.failure(NoInternetException()) // Devolver error si no hay conexión
        }
    }
}
