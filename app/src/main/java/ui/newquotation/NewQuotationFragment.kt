// ui/newquotation/NewQuotationFragment.kt
package ui.newquotation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dadm.roreizq.QuotationShake.R
import dadm.roreizq.QuotationShake.databinding.FragmentNewQuotationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import utils.NoInternetException

@AndroidEntryPoint
class NewQuotationFragment : Fragment(R.layout.fragment_new_quotation), MenuProvider {

    private val viewModel: NewQuotationViewModel by viewModels()
    private var _binding: FragmentNewQuotationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewQuotationBinding.bind(view)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Observar cambios en el nombre de usuario
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userName.collect { userName ->
                    val displayName = if (userName.isEmpty()) {
                        getString(R.string.anonymous)
                    } else {
                        userName
                    }
                    binding.tvGreetings.text = getString(R.string.greetings, displayName)
                }
            }
        }

        // Observar cambios en la cita y el estado de carga
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.quotation.collect { quotation ->
                    if (quotation != null) {
                        binding.tvQuotationText.text = quotation.text
                        binding.tvQuotationAuthor.text = quotation.author.ifEmpty {
                            getString(R.string.anonymous)
                        }
                        binding.tvGreetings.isVisible = false // Ocultar el mensaje de bienvenida
                    }
                }
            }
        }

        // Observar cambios en el estado de carga
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect { isLoading ->
                    binding.swipeRefreshLayout.isRefreshing = isLoading
                }
            }
        }

        // Observar cambios en la visibilidad del botón flotante
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFabVisible.collect { isVisible ->
                    binding.fabAddToFavourites.isVisible = isVisible
                }
            }
        }

        // Observar cambios en el estado de error
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorState.collect { error ->
                    error?.let {
                        val message = when (it) {
                            is NoInternetException -> getString(R.string.no_internet_error)
                            is java.io.IOException -> getString(R.string.network_error)
                            is IllegalStateException -> getString(R.string.server_error)
                            else -> getString(R.string.unknown_error)
                        }
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                        viewModel.resetError() // Reiniciar el estado de error
                    }
                }
            }
        }

        // Asociar OnRefreshListener al SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getNewQuotation()
        }

        // Asociar OnClickListener al botón flotante
        binding.fabAddToFavourites.setOnClickListener {
            viewModel.addToFavourites()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflar el menú
        menuInflater.inflate(R.menu.menu_new_quotation, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Manejar la selección del ítem del menú
        return when (menuItem.itemId) {
            R.id.action_refresh -> {
                viewModel.getNewQuotation() // Obtener una nueva cita
                true
            }
            else -> false
        }
    }
}