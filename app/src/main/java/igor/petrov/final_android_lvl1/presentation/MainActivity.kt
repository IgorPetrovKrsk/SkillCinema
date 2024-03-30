package igor.petrov.final_android_lvl1.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.WelcomeState
import igor.petrov.final_android_lvl1.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_user
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)


        //откроем меню приветствия
        lifecycleScope.launch {
            mainViewModel.welcomeState.collect {
                if (it == WelcomeState.Welcome ){
                    supportActionBar?.hide()
                    binding.navView.isVisible = false
                    binding.navView
                }else{
                    sharedPreferences.edit().putBoolean(SHARED_PREFERENCE_WELCOME_COMPLETE,true).apply() //запомним что уже запускали приветсвие
                    supportActionBar?.show()
                    binding.navView.isVisible = true
                }
            }
        }

        val isWelcomeComplete = sharedPreferences.getBoolean(SHARED_PREFERENCE_WELCOME_COMPLETE,false)
        navController.navigate(if (isWelcomeComplete) R.id.action_navigation_home_to_fragmentWelcomeLoading else R.id.action_navigation_home_to_fragmentWelcome1)

        navController.addOnDestinationChangedListener{ controller, destination, arguments ->
            supportActionBar?.title = arguments?.getString("label")?:destination.label
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    companion object{
        private const val SHARED_PREFERENCE_KEY =  "SHARED_PREFERENCE_KEY"
        private const val SHARED_PREFERENCE_WELCOME_COMPLETE =  "SHARED_PREFERENCE_WELCOME_COMPLETE_KEY"
    }


}