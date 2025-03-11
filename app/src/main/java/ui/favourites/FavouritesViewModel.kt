package ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.favourites.FavouritesRepository
import domain.model.Quotation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    // Se obtiene la lista de citas favoritas del repositorio como un StateFlow
    val favoriteQuotations: StateFlow<List<Quotation>> = favouritesRepository.getAllQuotations()
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed()
        )

    // Se ha eliminado el metodo loadFavourites(), ya que ahora los datos vienen del repositorio.

    // Se comenta la función para eliminar todas las citas, ya que debe corregirse.
    /*
    fun deleteAllQuotations() {
        viewModelScope.launch {
            favouritesRepository.deleteAllQuotations()
        }
    }
    */
    fun deleteQuotationByPosition(position: Int) {
        viewModelScope.launch {
            // Obtener la cita en la posición específica
            val quotation = favouritesRepository.getQuotationByPosition(position)
            // Si la cita existe, eliminarla
            quotation?.let {
                favouritesRepository.deleteQuotation(it)
            }
        }
    }

    fun deleteAllQuotations() {
        viewModelScope.launch {
            favouritesRepository.deleteAllQuotations()
        }
    }
}
