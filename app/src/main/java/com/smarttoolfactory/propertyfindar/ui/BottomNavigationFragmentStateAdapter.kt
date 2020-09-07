package com.smarttoolfactory.propertyfindar.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.smarttoolfactory.core.ui.adapter.NavigableFragmentStateAdapter
import com.smarttoolfactory.core.ui.fragment.navhost.NavHostContainerFragment
import com.smarttoolfactory.propertyfindar.R

/**
 * FragmentStateAdapter to contain ViewPager2 fragments inside another fragment which uses
 * wrapper layouts that contain [FragmentContainerView]
 *
 * * 🔥 Create FragmentStateAdapter with viewLifeCycleOwner instead of Fragment to make sure
 * that it lives between [Fragment.onCreateView] and [Fragment.onDestroyView] while [View] is alive
 *
 * * https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
 */
class BottomNavigationFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    NavigableFragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_home,
                R.id.nested_nav_host_fragment_home
            )

            // Vertical NavHost Post Fragment Container
            1 -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_favorites,
                R.id.nested_nav_host_fragment_favorites
            )

            // Horizontal NavHost Post Fragment Container
            2 -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_notification,
                R.id.nested_nav_host_fragment_notification
            )

            else -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_account,
                R.id.nested_nav_host_fragment_account
            )
        }
    }
}
