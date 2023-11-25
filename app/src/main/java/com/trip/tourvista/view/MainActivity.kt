package com.trip.tourvista.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.motion.utils.ViewState
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.trip.tourvista.R
import com.trip.tourvista.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private var navController: NavController? = null

    private val exceptionsNavFragments = listOf(R.id.tourFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()
        initNavListener()
    }



    private fun initNavController(){
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.navigation.apply {
            setupWithNavController(navController!!)
            itemIconTintList = null
        }
    }

    private fun initNavListener(){
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.navigation.isVisible = !exceptionsNavFragments.contains(destination.id)
        }
    }


}
