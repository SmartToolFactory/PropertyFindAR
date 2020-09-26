package com.smarttoolfactory.core.ui.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter.FragmentTransactionCallback.OnPostEventListener

/**
 * FragmentStateAdapter to add ability to set primary navigation fragment
 * which lets fragment visible to be navigable when back button is pressed using
 * [FragmentStateAdapter.FragmentTransactionCallback] in [ViewPager2].
 *
 * * 🔥 Create FragmentStateAdapter with viewLifeCycleOwner instead of Fragment to make sure
 * that it lives between [Fragment.onCreateView] and [Fragment.onDestroyView] while [View] is alive
 *
 * * https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
 */
abstract class NavigableFragmentStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentTransactionCallback =
        object : FragmentStateAdapter.FragmentTransactionCallback() {
            override fun onFragmentMaxLifecyclePreUpdated(
                fragment: Fragment,
                maxLifecycleState: Lifecycle.State
            ) = if (maxLifecycleState == Lifecycle.State.RESUMED) {

                // This fragment is becoming the active Fragment - set it to
                // the primary navigation fragment in the OnPostEventListener
                OnPostEventListener {
                    fragment.parentFragmentManager.commitNow {
                        setPrimaryNavigationFragment(fragment)
                    }
                }
            } else {
                super.onFragmentMaxLifecyclePreUpdated(fragment, maxLifecycleState)
            }
        }

    init {
        // Add a FragmentTransactionCallback to handle changing
        // the primary navigation fragment
        registerFragmentTransactionCallback()
    }

    fun registerFragmentTransactionCallback() {
        registerFragmentTransactionCallback(fragmentTransactionCallback)
    }

    fun unregisterFragmentTransactionCallback() {
        unregisterFragmentTransactionCallback(fragmentTransactionCallback)
    }
}
