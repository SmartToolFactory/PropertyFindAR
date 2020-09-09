package com.smarttoolfactory.propertyfindar

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.dynamicfeatures.fragment.DynamicNavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smarttoolfactory.core.ui.fragment.DynamicNavigationFragment
import com.smarttoolfactory.core.ui.fragment.navhost.BaseDynamicNavHostFragment
import com.smarttoolfactory.core.util.observe
import com.smarttoolfactory.core.util.setupWithNavController
import com.smarttoolfactory.core.viewmodel.PropertyDetailNavigationVM
import com.smarttoolfactory.propertyfindar.databinding.FragmentMainBottomNavBinding

/**
 * Alternative of MainFragment with only [BottomNavigationView]
 * that has [DynamicNavHostFragment]s as root fragment of each
 * tab with [BaseDynamicNavHostFragment]s that extend [DynamicNavHostFragment].
 *
 *
 */
class MainFragmentBottomNav : DynamicNavigationFragment<FragmentMainBottomNavBinding>() {

    /**
     * ViewModel for navigating to property detail screen from Main Fragment
     */
    private val propertyDetailNavigationVM by activityViewModels<PropertyDetailNavigationVM>()

    override fun getLayoutRes(): Int = R.layout.fragment_main_bottom_nav

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        subscribePropertyDetailNavigation()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val bottomNavigationView = dataBinding!!.bottomNav

        val navGraphIds = listOf(
            R.navigation.nav_graph_dfm_home_start,
            R.navigation.nav_graph_dfm_favorites_start,
            R.navigation.nav_graph_dfm_notification_start,
            R.navigation.nav_graph_dfm_account_start
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.nav_host_container,
            intent = requireActivity().intent
        )
    }

    /**
     * Navigates to Property Detail fragment from this fragment that replacing main fragment
     * that contains [BottomNavigationView]
     */
    private fun subscribePropertyDetailNavigation() {

        viewLifecycleOwner.observe(propertyDetailNavigationVM.goToPropertyDetailFromMain) {

            it.getContentIfNotHandled()?.let { propertyItem ->
                val bundle = bundleOf("property" to propertyItem)

                findNavController()
                    .navigate(
                        R.id.action_mainFragment_to_propertyDetailFragment,
                        bundle
                    )
            }
        }
    }
}
