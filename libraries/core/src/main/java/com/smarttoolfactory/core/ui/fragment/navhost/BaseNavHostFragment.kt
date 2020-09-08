package com.smarttoolfactory.core.ui.fragment.navhost

import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.smarttoolfactory.core.error.NavigationException
import com.smarttoolfactory.core.util.Event
import com.smarttoolfactory.core.viewmodel.NavControllerViewModel

/**
 * [NavHostFragment] creator class which
 * uses [BaseNavHostFragment.createNavHostFragment] function with navigation graph
 * parameter.
 *
 * ### Parameter keys are taken from [NavHostFragment] so it's not necessary to
 *  call onCreate to set current graph. [NavHostFragment] takes care of setting navigation graph.
 */
class BaseNavHostFragment : NavHostFragment() {

    private val navControllerViewModel by activityViewModels<NavControllerViewModel>()

    override fun onResume() {
        super.onResume()
        // Set this navController as ViewModel's navController
        navControllerViewModel.currentNavController.value = Event(navController)
    }

    override fun onDestroyView() {
        navControllerViewModel.currentNavController.value = Event(null)
        super.onDestroyView()
    }

    companion object {

        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"
        private const val KEY_START_DESTINATION_ARGS =
            "android-support-nav:fragment:startDestinationArgs"

        /**
         * Create a new NavHostFragment instance with an inflated [NavGraph] resource.
         *
         * @param graphResId resource id of the navigation graph to inflate
         * @param startDestinationArgs arguments to send to the start destination of the graph
         * @return a new NavHostFragment instance
         */

        @JvmStatic
        fun createNavHostFragment(
            @NavigationRes graphResId: Int,
            startDestinationArgs: Bundle? = null
        ): BaseNavHostFragment {

            if (graphResId == 0) throw NavigationException("Navigation graph id cannot be 0")

            val bundle: Bundle = Bundle().apply {
                putInt(KEY_GRAPH_ID, graphResId)

                if (startDestinationArgs != null) {
                    putBundle(KEY_START_DESTINATION_ARGS, startDestinationArgs)
                }
            }

            return BaseNavHostFragment().apply {
                arguments = bundle
            }
        }
    }
}
