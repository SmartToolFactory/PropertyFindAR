package com.smarttoolfactory.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.smarttoolfactory.core.ui.adapter.NavigableFragmentStateAdapter
import com.smarttoolfactory.core.ui.fragment.navhost.NavHostContainerFragment
import com.smarttoolfactory.home.R

/**
 * FragmentStateAdapter to contain ViewPager2 fragments inside another fragment.
 *
 * * 🔥 Create FragmentStateAdapter with viewLifeCycleOwner instead of Fragment to make sure
 * that it lives between [Fragment.onCreateView] and [Fragment.onDestroyView] while [View] is alive
 *
 * * https://stackoverflow.com/questions/61779776/leak-canary-detects-memory-leaks-for-tablayout-with-viewpager2
 */
class HomeViewPager2FragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    NavigableFragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_property_list_flow,
                R.id.nested_nav_host_fragment_property_list
            )

            // Fragment with RxJava3
            1 -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_property_list_rxjava3,
                R.id.nested_nav_host_fragment_property_list
            )

            //  Fragment with Flow + Pagination
            else -> NavHostContainerFragment.createNavHostContainerFragment(
                R.layout.fragment_navhost_property_list_paged,
                R.id.nested_nav_host_fragment_property_list
            )
        }
    }
}
