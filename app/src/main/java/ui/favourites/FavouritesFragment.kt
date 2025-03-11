// ui/favourites/FavouritesFragment.kt
package ui.favourites

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dadm.roreizq.QuotationShake.R
import dadm.roreizq.QuotationShake.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites), MenuProvider {
    private val viewModel: FavouritesViewModel by activityViewModels()
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    // Variable para almacenar el estado de las citas
    private var hasQuotations = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavouritesBinding.bind(view)

        // Configurar RecyclerView
        binding.rvFavourites.layoutManager = LinearLayoutManager(requireContext())
        val adapter = QuotationListAdapter { author ->
            if (author == "Anonymous") {
                Snackbar.make(binding.root, "Cannot show information for anonymous authors", Snackbar.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://en.wikipedia.org/wiki/Special:Search?search=$author")
                }
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Snackbar.make(binding.root, "No app available to handle this request", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.rvFavourites.adapter = adapter

        // Configurar ItemTouchHelper para deslizar y eliminar
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, // No soportamos arrastrar
            ItemTouchHelper.RIGHT // Solo soportamos deslizar hacia la derecha
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No permitimos mover elementos
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Obtener la posición del elemento deslizado
                val position = viewHolder.adapterPosition
                // Eliminar la cita en esa posición
                viewModel.deleteQuotationByPosition(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvFavourites)

        // Observar cambios en las citas
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoriteQuotations.collect { quotations ->
                    adapter.submitList(quotations)
                    // Actualizar el estado de las citas
                    hasQuotations = quotations.isNotEmpty()
                    // Invalidar el menú para actualizar su estado
                    requireActivity().invalidateOptionsMenu()
                }
            }
        }

        // Registrar MenuProvider
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_favourites, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        // Habilitar o deshabilitar la opción "Delete all quotations" según el estado de las citas
        val deleteAllItem = menu.findItem(R.id.action_delete_all)
        deleteAllItem.isEnabled = hasQuotations
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_delete_all -> {
                findNavController().navigate(R.id.action_to_deleteAllDialog)
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}