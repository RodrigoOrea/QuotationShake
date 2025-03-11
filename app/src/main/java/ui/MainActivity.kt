package ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dadm.roreizq.QuotationShake.R
import dadm.roreizq.QuotationShake.databinding.ActivityMainBinding
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuProvider {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newQuotationFragment,
                R.id.favouritesFragment,
                R.id.settingsFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        addMenuProvider(this, this, Lifecycle.State.RESUMED)

        val navigationBarView = binding.root.findViewById<NavigationBarView>(R.id.bottomNavigationView)
        navigationBarView.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())

            // Aplicar insets al BottomNavigationView
            binding.bottomNavigationView.updatePadding(
                left = systemBars.left,
                top = 0,
                right = 0,
                bottom = systemBars.bottom
            )

            // Aplicar insets al FragmentContainerView
            binding.navHostFragment.updatePadding(
                left = 0,
                top = 0,
                right = systemBars.right,
                bottom = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) systemBars.bottom else 0
            )

            // Aplicar insets al AppBarLayout
            binding.appbar.updatePadding(
                top = systemBars.top,
                left = systemBars.left,
                right = 0,
                bottom = 0
            )

            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflar el menú
        menuInflater.inflate(R.menu.menu_about, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Manejar la selección del ítem del menú
        return when (menuItem.itemId) {
            R.id.about -> {
                // Navegar al fragmento de "About"
                navController.navigate(R.id.aboutDialogFragment)
                true
            }
            else -> false
        }
    }
}