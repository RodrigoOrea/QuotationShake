package ui.newquotation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.newquotation.NewQuotationRepository
import domain.model.Quotation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import data.favourites.FavouritesRepository
import data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
//import kotlin.random.Random

@HiltViewModel
class NewQuotationViewModel @Inject constructor(
    private val repository: NewQuotationRepository, // Repositorio inyectado
    private val settingsRepository: SettingsRepository,
    private val favouritesRepository: FavouritesRepository // Repositorio de favoritos inyectado
) : ViewModel() {

    // Propiedad para el nombre de usuario
    private val _userName = MutableStateFlow(getUserName())
    val userName: StateFlow<String> = settingsRepository.getUserName()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    // Propiedad para la cita actual
    private val _quotation = MutableStateFlow<Quotation?>(null)
    val quotation: StateFlow<Quotation?> = _quotation.asStateFlow()

    // Propiedad para el estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Propiedad para el estado de error
    private val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState: StateFlow<Throwable?> = _errorState.asStateFlow()

    // Propiedad para la visibilidad del botón flotante
    val isFabVisible: StateFlow<Boolean> = quotation
        .flatMapLatest { currentQuotation ->
            if (currentQuotation == null) {
                flowOf(false)
            } else {
                favouritesRepository.getQuotationById(currentQuotation.id)
                    .map { quotationInDatabase ->
                        quotationInDatabase == null
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Metodo para obtener un nombre de usuario aleatorio
    private fun getUserName(): String {
        return "Mamá"
    }

    // Metodo para obtener una nueva cita
    fun getNewQuotation() {
        _isLoading.update { true } // Mostrar el icono de carga

        viewModelScope.launch {
            try {
                // Código de prueba (50% del servidor, 50% manual)
                /*
                val newQuotation = if (Random.nextBoolean()) {
                    repository.getNewQuotation().getOrThrow()
                } else {
                    // Cita manual de prueba
                    Quotation(
                        id = "manual_quote_1",
                        text = "Esta es una cita de prueba.",
                        author = "Autor de prueba"
                    )
                }
                _quotation.update { newQuotation }
                */

                // Código real (siempre del servidor)
                repository.getNewQuotation().fold(
                    onSuccess = { quotation ->
                        _quotation.update { quotation }
                    },
                    onFailure = { error ->
                        _errorState.update { error } // Almacenar el error
                    }
                )
            } finally {
                _isLoading.update { false } // Ocultar el icono de carga
            }
        }
    }

    // Metodo para añadir la cita a favoritos
    fun addToFavourites() {
        viewModelScope.launch {
            _quotation.value?.let { quotation ->
                favouritesRepository.insertQuotation(quotation)
            }
        }
    }

    // Metodo para reiniciar el estado de error
    fun resetError() {
        _errorState.update { null }
    }
}