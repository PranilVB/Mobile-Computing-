package com.example.smartqueue

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.smartqueue.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Single-activity host.
 * Routes to user nav or admin nav based on role stored in Firestore.
 * Bottom nav menu is set dynamically after role is determined.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val auth = FirebaseAuth.getInstance()

    // Fragments where the bottom nav should be hidden
    private val hideNavDestinations = setOf(
        R.id.loginFragment,
        R.id.registerFragment,
        R.id.serviceDetailFragment,
        R.id.adminQueueDetailFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        setupBottomNavVisibility()
        resolveStartDestination()
    }

    private fun setupBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in hideNavDestinations) {
                binding.bottomNav.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Check if user is already signed in and determine their role.
     * Phase 1: defaults to user role for mock data preview.
     * Phase 2: reads role from Firestore users collection.
     */
    private fun resolveStartDestination() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            setupUserNav()
            return
        }

        // Phase 2: Read role from Firestore
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { doc ->
                val role = doc.getString("role") ?: "user"
                if (role == "admin") {
                    setupAdminNav()
                    navController.navigate(R.id.admin_nav)
                } else {
                    setupUserNav()
                    navController.navigate(R.id.user_nav)
                }
            }
            .addOnFailureListener {
                setupUserNav()
                navController.navigate(R.id.user_nav)
            }
    }

    private fun setupUserNav() {
        binding.bottomNav.inflateMenu(R.menu.bottom_nav_user)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun setupAdminNav() {
        binding.bottomNav.inflateMenu(R.menu.bottom_nav_admin)
        binding.bottomNav.setupWithNavController(navController)
    }
}
